package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.entity.Shelters;

import java.util.Collection;

@Repository
public interface SheltersRepository extends JpaRepository<Shelters, Long> {
}
