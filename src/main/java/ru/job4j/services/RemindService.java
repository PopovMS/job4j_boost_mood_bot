package ru.job4j.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.content.Content;
import ru.job4j.sending.SentContent;
import ru.job4j.store.UserRepository;

import java.util.List;

@Service
public class RemindService {
    private final SentContent sentContent;
    private final UserRepository userRepository;

    public RemindService(SentContent sentContent, UserRepository userRepository) {
        this.sentContent = sentContent;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRateString = "${remind.period}")
    public void ping() {
        var temp = userRepository.findAll();
        System.out.println(temp.size());
        for (var user : userRepository.findAll()) {
            var content = new Content(user.getChatId());
            content.setText("Ping");
            sentContent.sent(content);
        }
    }
}
