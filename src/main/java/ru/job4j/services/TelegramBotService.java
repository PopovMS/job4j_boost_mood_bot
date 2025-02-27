package ru.job4j.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.config.OnRealCondition;
import ru.job4j.content.Content;
import ru.job4j.sending.SentContent;

@Service("TelegramBot")
@Conditional(OnRealCondition.class)
public class TelegramBotService extends TelegramLongPollingBot implements SentContent {
    private final BotCommandHandler handler;
    private final String botName;

    public TelegramBotService(@Value("${telegram.bot.name}") String botName,
                              @Value("${telegram.bot.token}") String botToken,
                              BotCommandHandler handler) {
        super(botToken);
        this.handler = handler;
        this.botName = botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handler.handleCallback(update.getCallbackQuery())
                    .ifPresent(this::sent);
        } else if (update.hasMessage() && update.getMessage().getText() != null) {
            handler.commands(update.getMessage())
                    .ifPresent(this::sent);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void sent(Content content) {
        try {
            if (content.getAudio() != null) {
                SendAudio sendAudio = new SendAudio();
                sendAudio.setAudio(content.getPhoto());
                sendAudio.setChatId(content.getChatId());
                if (!content.getText().isEmpty()) {
                    sendAudio.setTitle(content.getText());
                }
                this.execute(sendAudio);
            } else if (content.getPhoto() != null) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setPhoto(content.getPhoto());
                sendPhoto.setChatId(content.getChatId());
                this.execute(sendPhoto);
            } else if (content.getMarkup() != null) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setReplyMarkup(content.getMarkup());
                sendMessage.setChatId(content.getChatId());
                sendMessage.setText(content.getText());
                this.execute(sendMessage);
            } else if (content.getText() != null) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(content.getText());
                sendMessage.setChatId(content.getChatId());
                this.execute(sendMessage);
            }
        } catch (TelegramApiException e) {
                throw new SentContentException(e.getMessage(), e);
        }
    }
}
