package ru.job4j.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.job4j.store.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {TgUI.class, MoodFakeRepository.class})
class TgUITest {
    @Autowired
    @Qualifier("moodFakeRepository")
    private MoodRepository moodRepository;

    @Test
    public void whenBtnGood() {
        assertThat(moodRepository).isNotNull();
    }
}
