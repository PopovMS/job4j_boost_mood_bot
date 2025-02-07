package ru.job4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.job4j.model.*;
import ru.job4j.services.TgRemoteService;
import ru.job4j.store.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

@EnableScheduling
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner loadDatabase(MoodRepository moodRepository,
                                   MoodContentRepository moodContentRepository,
                                   AwardRepository awardRepository) {
        return args -> {
            var moods = moodRepository.findAll();
            if (!moods.isEmpty()) {
                return;
            }
            var data = new ArrayList<MoodContent>();
            try (
                    BufferedReader read = new BufferedReader(new FileReader("./MoodContent.txt"))) {
                read.lines()
                        .map(s -> s.split(":"))
                        .filter(array -> array.length > 1)
                        .forEach(vol -> {
                            data.add(new MoodContent(new Mood(vol[0], true), vol[1]));
                                }
                        );
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
            moodRepository.saveAll(data.stream().map(MoodContent::getMood).toList());
            moodContentRepository.saveAll(data);

            // Инициализация наград
            var awards = new ArrayList<Award>();
            try (
                    BufferedReader read = new BufferedReader(new FileReader("./Award.txt"))) {
                read.lines()
                        .map(s -> s.split(":"))
                        .filter(array -> array.length > 1)
                        .forEach(vol -> {
                                    awards.add(new Award(vol[0], vol[1], Integer.parseInt(vol[2])));
                                }
                        );
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
            awardRepository.saveAll(awards);
        };
    }
}
