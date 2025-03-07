package ru.job4j.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotOptions;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import ru.job4j.config.OnFakeCondition;
import ru.job4j.content.Content;
import ru.job4j.sending.SentContent;

@Service("TelegramBot")
@Conditional(OnFakeCondition.class)
public class TelegramBotFakeService implements SentContent, LongPollingBot {
    private final String botName;

    public TelegramBotFakeService(@Value("${telegram.bot.name}") String botName) {
        this.botName = botName;
    }

    @Override
    public void sent(Content content) {
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    @Override
    public BotOptions getOptions() {
        return null;
    }

    @Override
    public void clearWebhook() throws TelegramApiRequestException {

    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return null;
    }
}
