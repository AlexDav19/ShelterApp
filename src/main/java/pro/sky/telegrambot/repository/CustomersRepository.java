package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.entity.Customers;

import java.util.List;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, Long> {
    List<Customers> findByNameAndPhone(String name, String phone);

    Customers findByChatId(Long chatId);
}
