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
    private String phoneMain;
    private String phoneSecurity;

    public Shelters(String address, String workingHours, String drivingDirections, String phoneMain, String phoneSecurity) {
        this.address = address;
        this.workingHours = workingHours;
        this.drivingDirections = drivingDirections;
        this.phoneMain = phoneMain;
        this.phoneSecurity = phoneSecurity;
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

    public String getPhoneMain() {
        return phoneMain;
    }

    public String getPhoneSecurity() {
        return phoneSecurity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelters shelters = (Shelters) o;
        return Objects.equals(id, shelters.id) && Objects.equals(address, shelters.address) && Objects.equals(workingHours, shelters.workingHours) && Objects.equals(drivingDirections, shelters.drivingDirections) && Objects.equals(phoneMain, shelters.phoneMain) && Objects.equals(phoneSecurity, shelters.phoneSecurity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, workingHours, drivingDirections, phoneMain, phoneSecurity);
    }

    @Override
    public String toString() {
        return "Shelters{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", workingHours='" + workingHours + '\'' +
                ", drivingDirections='" + drivingDirections + '\'' +
                ", phoneMain='" + phoneMain + '\'' +
                ", phoneSecurity='" + phoneSecurity + '\'' +
                '}';
    }
}
