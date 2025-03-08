package ru.job4j.action;

import ru.job4j.content.Content;

import java.util.Optional;

public interface HandleCommand {
    String name();

    Optional<Content> execute(long chatId, Long userId);
}
