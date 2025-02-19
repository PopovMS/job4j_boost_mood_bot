package ru.job4j.services;


import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.job4j.content.*;
import ru.job4j.model.*;
import ru.job4j.store.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MoodService {
    private final ApplicationEventPublisher publisher;
    private final MoodLogRepository moodLogRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository,
                       ApplicationEventPublisher publisher) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.publisher = publisher;
    }

    public Content chooseMood(User user, Long moodId) {
        Mood mood = new Mood();
        mood.setId(moodId);
        MoodLog moodLog = new MoodLog();
        LocalDateTime currentDateTime = LocalDateTime.now();
        long currentDateTimeFormat = Long.parseLong(currentDateTime.format(formatter));
        moodLog.setMood(mood);
        moodLog.setUser(user);
        moodLog.setCreatedAt(currentDateTimeFormat);
        moodLogRepository.save(moodLog);
        publisher.publishEvent(new UserEvent(this, user));
        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        LocalDateTime weeklyLog = LocalDateTime.now().minusDays(7);
        long weeklyLogDate = Long.parseLong(weeklyLog.format(formatter));
        List<MoodLog> filteredLog = moodLogRepository.findAll().stream()
                .filter(vol -> vol.getUser().getClientId() == clientId)
                .filter(vol -> vol.getCreatedAt() >= weeklyLogDate)
                .toList();
        var content = new Content(chatId);
        content.setText(formatMoodLogs(filteredLog, "Список настроений за 7 дней"));
        return Optional.of(content);
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        LocalDateTime monthlyLog = LocalDateTime.now().minusDays(30);
        long monthlyLogDate = Long.parseLong(monthlyLog.format(formatter));
        List<MoodLog> filteredLog = moodLogRepository.findAll().stream()
                .filter(vol -> vol.getUser().getClientId() == clientId)
                .filter(vol -> vol.getCreatedAt() >= monthlyLogDate)
                .toList();
        var content = new Content(chatId);
        content.setText(formatMoodLogs(filteredLog, "Список настроений за месяц"));
        return Optional.of(content);
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return String.format("%s:%n%s", title, "Список настроений не найден");
        }
        var result = new StringBuilder(String.format("%s%n", title));
        logs.forEach(log -> {
            String formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreatedAt()));
            result.append(formattedDate).append(": ").append(log.getMood().getText()).append(System.lineSeparator());
        });
        return result.toString();
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        List<Achievement> achievements = achievementRepository.findAll().stream()
                .filter(vol -> vol.getUser().getClientId() == clientId)
                .toList();
        var content = new Content(chatId);
        content.setText(formatAwardLogs(achievements, "Награды"));
        return Optional.of(content);
    }

    private String formatAwardLogs(List<Achievement> logs, String title) {
        if (logs.isEmpty()) {
            return String.format("%s:%n%s", title, "Награды не найдены");
        }
        var result = new StringBuilder(String.format("%s%n", title));
        logs.forEach(log -> {
            String formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreateAt()));
            result.append(formattedDate).append(": ").append(log.getAward().getTitle()).append(System.lineSeparator());
        });
        return result.toString();
    }
}

