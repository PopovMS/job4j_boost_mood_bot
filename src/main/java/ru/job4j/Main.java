package ru.job4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
;
import ru.job4j.model.*;

import ru.job4j.store.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
                        .filter(array -> array.length > 2)
                        .forEach(vol -> {
                            data.add(new MoodContent(new Mood(vol[0], Boolean.valueOf(vol[2])), vol[1]));
                                }
                        );
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
            moodRepository.saveAll(data.stream().map(MoodContent::getMood).toList());
            moodContentRepository.saveAll(data);
            var awards = new ArrayList<Award>();
            try (
                    BufferedReader read = new BufferedReader(new FileReader("./Award.txt"))) {
                read.lines()
                        .map(s -> s.split(":"))
                        .filter(array -> array.length > 2)
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
