package ru.job4j.store;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.model.Mood;
import ru.job4j.store.MoodRepository;

import java.util.ArrayList;
import java.util.List;

public class MoodFakeRepository
        extends CrudRepositoryFake<Mood, Long>
        implements MoodRepository {

    public List<Mood> findAll() {
        return new ArrayList<>(memory.values());
    }
}
