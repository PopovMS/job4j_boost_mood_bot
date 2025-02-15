package ru.job4j.store;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.model.MoodLog;
import ru.job4j.model.User;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoodLogFakeRepository
        extends CrudRepositoryFake<MoodLog, Long>
        implements MoodLogRepository {

    public List<MoodLog> findAll() {
        return new ArrayList<>(memory.values());
    }

    @Override
    public List<User> findUsersWhoDidNotVoteToday(long startOfDay, long endOfDay) {
        return memory.values().stream()
                .filter(moodLog -> moodLog.getCreatedAt() <= startOfDay)
                .map(MoodLog::getUser)
                .distinct()
                .collect(Collectors.toList());
    }
}
