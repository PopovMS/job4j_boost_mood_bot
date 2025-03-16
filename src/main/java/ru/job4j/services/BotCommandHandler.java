package ru.job4j.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.job4j.action.*;
import ru.job4j.content.Content;
import ru.job4j.store.UserRepository;
import java.util.Optional;

@Service
public class BotCommandHandler {
    private final UserRepository userRepository;
    private final MoodService moodService;

    public BotCommandHandler(UserRepository userRepository,
                             MoodService moodService) {
        this.userRepository = userRepository;
        this.moodService = moodService;
    }

    Optional<Content> commands(Message message) {
        String command = message.getText();
        long chatId = message.getChatId();
        long userId = message.getFrom().getId();
        return UserAction.ACTIONLIST.stream()
                .filter(value -> command.equals(value.name()))
                .map(value ->   value.execute(chatId, userId))
                .findFirst()
                .orElse(Optional.empty());
    }

    Optional<Content> handleCallback(CallbackQuery callback) {
        var moodId = Long.valueOf(callback.getData());
        var user = userRepository.findByClientId(callback.getFrom().getId());
        return user.stream()
                .map(value -> moodService.chooseMood(value, moodId))
                .findFirst();
    }
}

