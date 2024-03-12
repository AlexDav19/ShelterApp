package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.entity.Adoptions;

public interface AdoptionsRepository extends JpaRepository<Adoptions, Long> {
}
