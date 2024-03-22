package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
