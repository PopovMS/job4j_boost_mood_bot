package ru.job4j.action;

import org.springframework.beans.factory.annotation.Autowired;
import ru.job4j.content.Content;
import ru.job4j.services.MoodService;

import java.util.Optional;

public class HandleAwardCommand implements HandleCommand {
    private final MoodService moodService;

    @Autowired
    public HandleAwardCommand(MoodService moodService) {
        this.moodService = moodService;
    }

    @Override
    public String name() {
        return "/award";
    }

    @Override
    public Optional<Content> execute(long chatId, Long userId) {
        return moodService.awards(chatId, userId);
    }
}
