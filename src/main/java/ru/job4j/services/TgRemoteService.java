package ru.job4j.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.model.User;
import ru.job4j.store.MoodRepository;
import ru.job4j.store.UserRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TgRemoteService extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;
    private final UserRepository userRepository;
    private final MoodRepository moodRepository;
    private static final Map<String, String> MOOD_RESP = new HashMap<>();

    public TgRemoteService(@Value("${telegram.bot.name}") String botName,
                           @Value("${telegram.bot.token}") String botToken,
                           UserRepository userRepository,
                            MoodRepository moodRepository) {
        this.botName = botName;
        this.botToken = botToken;
        this.userRepository = userRepository;
        this.moodRepository = moodRepository;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            var data = update.getCallbackQuery().getData();
            var chatId = update.getCallbackQuery().getMessage().getChatId();
            send(new SendMessage(String.valueOf(chatId), MOOD_RESP.get(data)));
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            if ("/start".equals(message.getText())) {
                long chatId = message.getChatId();
                var user = new User();
                user.setClientId(message.getFrom().getId());
                user.setChatId(chatId);
                userRepository.save(user);
                send(sendButtons(chatId));
            }

            long chatId = update.getMessage().getChatId();
            send(sendButtons(chatId));
        }
    }

    public void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public SendMessage sendButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Как настроение сегодня?");
        var buttons = new TgUI(moodRepository);
        message.setReplyMarkup(buttons.buildButtons());
        return message;
    }

    static {
        MOOD_RESP.put("lost_sock", "Носки — это коварные создания. Но не волнуйся, второй обязательно найдётся!");
        MOOD_RESP.put("cucumber", "Огурец тоже дело серьёзное! Главное, не мариноваться слишком долго.");
        MOOD_RESP.put("dance_ready", "Супер! Танцуй, как будто никто не смотрит. Или, наоборот, как будто все смотрят!");
        MOOD_RESP.put("need_coffee", "Кофе уже в пути! Осталось только подождать... И ещё немного подождать...");
        MOOD_RESP.put("sleepy", "Пора на боковую! Даже супергерои отдыхают, ты не исключение.");
    }
}