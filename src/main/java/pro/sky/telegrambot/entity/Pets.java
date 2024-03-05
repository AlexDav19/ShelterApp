package pro.sky.telegrambot.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class Pets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String breed;
    private int age;
    private String photo;

    public Pets(String name, String breed, int age, String photo) {
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.photo = photo;
    }

    public Pets() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public int getAge() {
        return age;
    }

    public String getPhoto() {
        return photo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pets pets = (Pets) o;
        return age == pets.age && Objects.equals(id, pets.id) && Objects.equals(name, pets.name) && Objects.equals(breed, pets.breed) && Objects.equals(photo, pets.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, breed, age, photo);
    }

    @Override
    public String toString() {
        return "Pets{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", photo='" + photo + '\'' +
                '}';
    }
}
