package pro.sky.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Customers;
import pro.sky.telegrambot.repository.CustomersRepository;

import java.util.Collection;

@Service
public class CustomersService {

    Logger logger = LoggerFactory.getLogger(PetsService.class);

    private final CustomersRepository customersRepository;

    public CustomersService(CustomersRepository customersRepository) {
        this.customersRepository = customersRepository;
    }

    public Collection<Customers> getAllCustomers() {
        logger.debug("Вызван метод getAllCustomers");
        return customersRepository.findAll();
    }

    public Customers getCustomerById(Long customerId) {
        logger.debug("Вызван метод getCustomerById");
        return customersRepository.findById(customerId).get();
    }

    public Customers createCustomer(Customers customer) {
        logger.debug("Вызван метод createCustomer");
        return customersRepository.save(customer);
    }

    public Customers updateCustomer(Long id, Customers customer) {
        logger.debug("Вызван метод updateCustomer");
        if (customersRepository.existsById(id)) {
            customer.setId(id);
            customersRepository.save(customer);
            return customer;
        } else {
            return null;
        }
    }

    public void deleteCustomer(Long customerId) {
        logger.debug("Вызван метод deleteCustomer");
        customersRepository.deleteById(customerId);
    }
}
