package ru.job4j.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.job4j.content.Content;
import ru.job4j.store.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {BotCommandHandler.class, UserFakeRepository.class,
        MoodFakeRepository.class, MoodLogFakeRepository.class})
class BotCommandHandlerTest {
    private final long chatId = 5L;
    private final long clientId = 10L;
    private final long moodId = 10L;
    private final long userId = 1L;

    @Autowired
    @Qualifier("userFakeRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("moodFakeRepository")
    private MoodRepository moodRepository;

    @Autowired
    @Qualifier("moodLogFakeRepository")
    private MoodLogRepository moodLogRepository;

    @MockBean
    private TgUI tgUI;

    @MockBean
    private MoodService moodService;

    @Autowired
    private BotCommandHandler botCommandHandler;

    @Test
    public void whenCommandStart() {
        Optional<Content> content;
        var expected = new Content(chatId);
        expected.setText("Как настроение?");
        userRepository.deleteAll();
        assertThat(userRepository.findByClientId(clientId).stream().findFirst()).isEmpty();
        var inline = new InlineKeyboardButton();
        inline.setText("Good");
        inline.setCallbackData(String.valueOf(1));
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(inline));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        Mockito.when(tgUI.buildButtons())
                .thenReturn(inlineKeyboardMarkup);
        expected.setMarkup(inlineKeyboardMarkup);
        content = botCommandHandler.commands(createTxtMessage("/start"));
        var user =  new ru.job4j.model.User(clientId, chatId);
        assertThat(userRepository.findByClientId(clientId).stream().findFirst().get()).isEqualTo(user);
        assertThat(content).isEqualTo(Optional.of(expected));
    }

    @Test
    public void whenCommandWeekMoodLog() {
        Optional<Content> content;
        var contentExpected = new Content(chatId);
        contentExpected.setText("Список настроений за 7 дней");
        Mockito.when(moodService.weekMoodLogCommand(chatId, clientId))
                .thenReturn(Optional.of(contentExpected));
        content = botCommandHandler.commands(createTxtMessage("/week_mood_log"));
        assertThat(content).isEqualTo(Optional.of(contentExpected));
    }

    @Test
    public void whenCommandMonthMoodLog() {
        Optional<Content> content;
        var contentExpected = new Content(chatId);
        contentExpected.setText("Список настроений за месяц");
        Mockito.when(moodService.monthMoodLogCommand(chatId, clientId))
                .thenReturn(Optional.of(contentExpected));
        content = botCommandHandler.commands(createTxtMessage("/month_mood_log"));
        assertThat(content).isEqualTo(Optional.of(contentExpected));
    }

    @Test
    public void whenCommandAward() {
        Optional<Content> content;
        var contentExpected = new Content(chatId);
        contentExpected.setText("Награды");
        Mockito.when(moodService.awards(chatId, clientId))
                .thenReturn(Optional.of(contentExpected));
        content = botCommandHandler.commands(createTxtMessage("/award"));
        assertThat(content).isEqualTo(Optional.of(contentExpected));
    }

    @Test
    public void whenHandleCallback() {
        var expected = new Content(chatId);
        expected.setText("Рекомендации:");
        var user =  new ru.job4j.model.User(clientId, chatId);
        userRepository.save(user);
        Mockito.when(moodService.chooseMood(user, moodId))
                .thenReturn(expected);
        Optional<Content> content;
        CallbackQuery callbackquery = new CallbackQuery();
        var tgApiUser = new User();
        tgApiUser.setId(clientId);
        callbackquery.setFrom(tgApiUser);
        callbackquery.setData(String.valueOf(moodId));
        callbackquery.setId("CLIENT_ID");
        content = botCommandHandler.handleCallback(callbackquery);
        assertThat(content).isEqualTo(Optional.of(expected));
    }

    @Test
    public void whenErrorCommand() {
        Optional<Content> content;
        content = botCommandHandler.commands(createTxtMessage("/errorText"));
        assertThat(content).isEmpty();
    }

    private Message createTxtMessage(String text) {
        var message = new Message();
        var chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);
        var user = new User();
        user.setId(clientId);
        message.setFrom(user);
        message.setText(text);
        return message;
    }
}