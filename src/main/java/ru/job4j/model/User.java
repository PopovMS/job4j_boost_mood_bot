package ru.job4j.model;

import java.util.Objects;
import jakarta.persistence.*;

@Entity
@Table(name = "mb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private long clientId;

    @Column(name = "chat_id")
    private long chatId;

    public User(Long id, long clientId, long chatId) {
        this.id = id;
        this.clientId = clientId;
        this.chatId = chatId;
    }

    public User(long clientId, long chatId) {
        this.clientId = clientId;
        this.chatId = chatId;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(clientId, user.clientId);
    }

    @Override
    public String toString() {
        return String.format("ID: %s, ClientID: %s, chatID: %s", id, clientId, chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}