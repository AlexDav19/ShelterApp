package pro.sky.telegrambot.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private Long adoption_id;
    private String text;
    private String photoId;

    public Report(Long adoption_id, String text, String photoId) {
        this.adoption_id = adoption_id;
        this.text = text;
        this.photoId = photoId;
    }

    public Report() {

    }

    public Long getId() {
        return id;
    }

    public Long getAdoption_id() {
        return adoption_id;
    }

    public String getText() {
        return text;
    }

    public String getPhotoId() {
        return photoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id) && Objects.equals(adoption_id, report.adoption_id) && Objects.equals(text, report.text) && Objects.equals(photoId, report.photoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, adoption_id, text, photoId);
    }

    @Override
    public String
    toString() {
        return "Report{" +
                "id=" + id +
                ", adoption_id=" + adoption_id +
                ", text='" + text + '\'' +
                ", photoId='" + photoId + '\'' +
                '}';
    }
}
