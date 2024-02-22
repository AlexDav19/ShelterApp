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
    private Long photoSize;
    private String photoType;
    @Lob
    private byte[] photoData;

    public Pets(String name, String breed, int age) {
        this.name = name;
        this.breed = breed;
        this.age = age;
    }

    public Pets() {
    }

    public Long getId() {
        return id;
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

    public Long getPhotoSize() {
        return photoSize;
    }

    public String getPhotoType() {
        return photoType;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pets pets = (Pets) o;
        return age == pets.age && Objects.equals(id, pets.id) && Objects.equals(name, pets.name) && Objects.equals(breed, pets.breed) && Objects.equals(photoSize, pets.photoSize) && Objects.equals(photoType, pets.photoType) && Arrays.equals(photoData, pets.photoData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, breed, age, photoSize, photoType);
        result = 31 * result + Arrays.hashCode(photoData);
        return result;
    }

    @Override
    public String toString() {
        return "Pets{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", photoSize=" + photoSize +
                ", photoType='" + photoType + '\'' +
                '}';
    }
}
