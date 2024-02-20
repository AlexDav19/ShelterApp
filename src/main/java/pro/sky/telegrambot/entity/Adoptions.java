package pro.sky.telegrambot.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Adoptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long adopterId;
    private Long petId;
    private LocalDateTime trialEnd;
    private LocalDateTime lastReport;
    private boolean trialSuccess;

    public Adoptions(Long adopterId, Long petId, LocalDateTime trialEnd) {
        this.adopterId = adopterId;
        this.petId = petId;
        this.trialEnd = trialEnd;
    }

    public Adoptions() {
    }

    public Long getId() {
        return id;
    }

    public Long getAdopterId() {
        return adopterId;
    }

    public Long getPetId() {
        return petId;
    }

    public LocalDateTime getTrialEnd() {
        return trialEnd;
    }

    public LocalDateTime getLastReport() {
        return lastReport;
    }

    public boolean isTrialSuccess() {
        return trialSuccess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adoptions adoptions = (Adoptions) o;
        return trialSuccess == adoptions.trialSuccess && Objects.equals(id, adoptions.id) && Objects.equals(adopterId, adoptions.adopterId) && Objects.equals(petId, adoptions.petId) && Objects.equals(trialEnd, adoptions.trialEnd) && Objects.equals(lastReport, adoptions.lastReport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, adopterId, petId, trialEnd, lastReport, trialSuccess);
    }

    @Override
    public String toString() {
        return "Adoptions{" +
                "id=" + id +
                ", adopterId=" + adopterId +
                ", petId=" + petId +
                ", trialEnd=" + trialEnd +
                ", lastReport=" + lastReport +
                ", trialSuccess=" + trialSuccess +
                '}';
    }
}
