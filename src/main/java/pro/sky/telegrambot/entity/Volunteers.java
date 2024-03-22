package pro.sky.telegrambot.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Volunteers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String firstName;
    private String userName;

    public Volunteers(Long chatId, String text, String userName) {
        this.chatId = chatId;
        this.firstName = text;
        this.userName = userName;

    }

    public Volunteers() {
    }


    public Long getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteers that = (Volunteers) o;
        return Objects.equals(id, that.id) && Objects.equals(chatId, that.chatId) && Objects.equals(firstName, that.firstName) && Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, firstName, userName);
    }

    @Override
    public String toString() {
        return "Volunteers{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", name='" + firstName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
