package ru.job4j.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.job4j.content.Content;
import ru.job4j.services.MoodService;

import java.util.Optional;

@Component
public class HandleWeekMoodLogCommand implements HandleCommand {

    private final MoodService moodService;

    @Autowired
    public HandleWeekMoodLogCommand(MoodService moodService) {
        this.moodService = moodService;
    }

    @Override
    public String name() {
        return "/week_mood_log";
    }

    @Override
    public Optional<Content> execute(long chatId, Long userId) {
        return moodService.weekMoodLogCommand(chatId, userId);
    }
}
