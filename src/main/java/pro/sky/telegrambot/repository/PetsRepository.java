package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.entity.Pets;

public interface PetsRepository extends JpaRepository<Pets, Long> {
}
