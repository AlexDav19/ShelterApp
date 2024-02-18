package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.entity.Volunteers;

import java.util.List;

@Repository
public interface VolunteersRepository extends JpaRepository<Volunteers, Long> {
}
