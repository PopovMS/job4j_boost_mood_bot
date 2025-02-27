package ru.job4j.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.job4j.content.Content;
import ru.job4j.model.User;
import ru.job4j.store.UserRepository;

import java.util.Optional;

@Service
public class BotCommandHandler {
    private final UserRepository userRepository;
    private final MoodService moodService;
    private final TgUI tgUI;

    public BotCommandHandler(UserRepository userRepository,
                             MoodService moodService,
                             TgUI tgUI) {
        this.userRepository = userRepository;
        this.moodService = moodService;
        this.tgUI = tgUI;
    }

    Optional<Content> commands(Message message) {
        String command = message.getText();
        long chatId = message.getChatId();
        long userId = message.getFrom().getId();
        return switch (command) {
                case "/start" -> handleStartCommand(chatId, userId);
                case "/week_mood_log" -> moodService.weekMoodLogCommand(chatId, userId);
                case "/month_mood_log" -> moodService.monthMoodLogCommand(chatId, userId);
                case "/award" -> moodService.awards(chatId, userId);
                default -> Optional.empty();
        };
    }

    Optional<Content> handleCallback(CallbackQuery callback) {
        var moodId = Long.valueOf(callback.getData());
        var user = userRepository.findByClientId(callback.getFrom().getId());
        return user.stream()
                .map(value -> moodService.chooseMood(value, moodId))
                .findFirst();
    }

    private Optional<Content> handleStartCommand(long chatId, Long clientId) {
        var user = new User();
        user.setClientId(clientId);
        user.setChatId(chatId);
        if (userRepository.findByClientId(clientId).isEmpty()) {
            userRepository.save(user);
        }
        var content = new Content(user.getChatId());
        content.setText("Как настроение?");
        content.setMarkup(tgUI.buildButtons());
        return Optional.of(content);
    }
}

