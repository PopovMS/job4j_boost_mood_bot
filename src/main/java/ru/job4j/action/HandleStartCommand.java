package ru.job4j.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.job4j.content.Content;
import ru.job4j.model.User;
import ru.job4j.services.TgUI;
import ru.job4j.store.UserRepository;
import java.util.Optional;

@Component
public class HandleStartCommand implements HandleCommand {
    private final UserRepository userRepository;
    private final TgUI tgUI;

    @Autowired
    public HandleStartCommand(UserRepository userRepository, TgUI tgUI) {
        this.userRepository = userRepository;
        this.tgUI = tgUI;
    }

    @Override
    public String name() {
        return "/start";
    }

    @Override
    public Optional<Content> execute(long chatId, Long userId) {
        var user = new User();
        user.setClientId(userId);
        user.setChatId(chatId);
        if (userRepository.findByClientId(userId).isEmpty()) {

            userRepository.save(user);
        }
        var content = new Content(user.getChatId());
        content.setText("Как настроение?");
        content.setMarkup(tgUI.buildButtons());
        return Optional.of(content);
    }
}
