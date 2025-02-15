package ru.job4j.store;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.model.MoodLog;
import ru.job4j.model.User;
import java.util.List;

@Repository

public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
    List<MoodLog> findAll();

    List<User> findUsersWhoDidNotVoteToday (long startOfDay, long endOfDay);
}
