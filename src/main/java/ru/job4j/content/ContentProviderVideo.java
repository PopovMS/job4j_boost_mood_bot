package ru.job4j.content;

import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;

public class ContentProviderVideo implements ContentProvider {
    @Override
    public Content byMood(Long chatId, Long moodId) {
        var content = new Content(chatId);
        content.setAudio(new InputFile(new File("./video/film.mp4")));
        return content;
    }
}
