package ru.job4j.services;


import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.content.Content;
import ru.job4j.model.Achievement;
import ru.job4j.model.Award;
import ru.job4j.model.MoodLog;
import ru.job4j.model.UserEvent;
import ru.job4j.sending.SentContent;
import ru.job4j.store.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class AchievementService implements ApplicationListener<UserEvent> {
    private final SentContent sentContent;
    private final AchievementRepository achievementRepository;
    private final MoodLogRepository moodlogRepository;
    private final AwardRepository awardRepository;

    public AchievementService(AchievementRepository achievementRepository,
                              MoodLogRepository moodlogRepository,
                              AwardRepository awardRepository,
                              SentContent sentContent) {
        this.achievementRepository = achievementRepository;
        this.moodlogRepository = moodlogRepository;
        this.awardRepository = awardRepository;
        this.sentContent = sentContent;
    }

    @Transactional
    @Override
    public void onApplicationEvent(UserEvent event) {
        var user = event.getUser();
        List<MoodLog> moodLogs = moodlogRepository.findAll().stream()
                .filter(value -> value.getUser().equals(user))
                .filter(value -> value.getMood().isGood())
                .toList();

        Optional<Award> award = awardRepository.findAll().stream()
                .filter(value -> value.getDays() == moodLogs.size())
                .findFirst();
        award.ifPresent(value -> achievementRepository.save(new Achievement(Instant.now().getEpochSecond(), user, value)));
        List<Achievement> achievements = achievementRepository.findAll().stream()
                .filter(value -> value.getUser().equals(user))
                .toList();
        var content = new Content(user.getChatId());
        if (!achievements.isEmpty()) {
            content.setText(String.format("УРА! Награды:%n"));
        } else {
            content.setText(String.format("Сегодня наград нет.%n"));
        }
        sentContent.sent(content);
        achievements.forEach(achievement -> {
            content.setText(String.format(" :%s, :%s%n", achievement.getAward().getDescription(),
                    achievement.getAward().getDays()));
            sentContent.sent(content);
        }
        );
    }
}
