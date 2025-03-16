package ru.job4j.content;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Objects;

public class Content {
    private final Long chatId;
    private String text;
    private InputFile photo;
    private InlineKeyboardMarkup markup;
    private ReplyKeyboardMarkup rMarkup;
    private InputFile audio;
    private InputFile video;

    public Content(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public InputFile getPhoto() {
        return photo;
    }

    public void setPhoto(InputFile photo) {
        this.photo = photo;
    }

    public InlineKeyboardMarkup getMarkup() {
        return markup;
    }

    public void setMarkup(InlineKeyboardMarkup markup) {
        this.markup = markup;
    }

    public ReplyKeyboardMarkup getRMarkup() {
        return rMarkup;
    }

    public void setRMarkup(ReplyKeyboardMarkup rMarkup) {
        this.rMarkup = rMarkup;
    }

    public InputFile getAudio() {
        return audio;
    }

    public void setAudio(InputFile audio) {
        this.audio = audio;
    }

    public InputFile getVideo() {
        return video;
    }

    public void setVideo(InputFile video) {
        this.video = video;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Content content = (Content) o;
        return (Objects.equals(chatId, content.chatId)
                && Objects.equals(text, content.text)
                && Objects.equals(markup, content.markup)
                && Objects.equals(rMarkup, content.rMarkup)
                && Objects.equals(audio, content.audio)
                && Objects.equals(photo, content.photo)
                && Objects.equals(video, content.video)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }

    @Override
    public String toString() {
        return "Content{"
                + "chatId=" + chatId
                + ", text='" + text + '\''
                + ", photo=" + photo
                + ", markup=" + markup
                + ", rMarkup=" + rMarkup
                + ", audio=" + audio
                + ", video=" + video
                + '}';
    }
}
