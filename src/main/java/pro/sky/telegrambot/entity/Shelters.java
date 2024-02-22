package pro.sky.telegrambot.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Shelters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private String workingHours;
    private String drivingDirections;

    public Shelters(String address, String workingHours, String drivingDirections) {
        this.address = address;
        this.workingHours = workingHours;
        this.drivingDirections = drivingDirections;
    }

    public Shelters() {

    }

    public Long getId() {
        return id;
    }
    public String getAddress() {
        return address;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public String getDrivingDirections() {
        return drivingDirections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelters shelters = (Shelters) o;
        return Objects.equals(id, shelters.id) && Objects.equals(address, shelters.address) && Objects.equals(workingHours, shelters.workingHours) && Objects.equals(drivingDirections, shelters.drivingDirections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, workingHours, drivingDirections);
    }

    @Override
    public String toString() {
        return "Shelters{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", workingHours='" + workingHours + '\'' +
                ", drivingDirections='" + drivingDirections + '\'' +
                '}';
    }
}
