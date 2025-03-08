package ru.job4j.action;

import org.springframework.beans.factory.annotation.Autowired;
import ru.job4j.content.Content;
import ru.job4j.services.MoodService;

import java.util.Optional;

public class HandleMonthMoodLogCommand implements HandleCommand {
    private final MoodService moodService;

    @Autowired
    public HandleMonthMoodLogCommand(MoodService moodService) {
        this.moodService = moodService;
    }

    @Override
    public String name() {
        return "/month_mood_log";
    }

    @Override
    public Optional<Content> execute(long chatId, Long userId) {
        return moodService.monthMoodLogCommand(chatId, userId);
    }
}
