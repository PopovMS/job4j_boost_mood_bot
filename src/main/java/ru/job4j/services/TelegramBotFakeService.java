package ru.job4j.services;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.config.OnFakeCondition;
import ru.job4j.content.Content;
import ru.job4j.sending.SentContent;

@Service("TelegramBot")
@Conditional(OnFakeCondition.class)
public class TelegramBotFakeService extends TelegramLongPollingBot implements SentContent {
    private static final String BOT_NAME = "FakeName";
    private static final String BOT_TOKEN = "token";

    @Override
    public void sent(Content content) {
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
