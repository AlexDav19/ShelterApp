package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.entity.Customers;
import pro.sky.telegrambot.entity.Pets;
import pro.sky.telegrambot.service.CustomersService;

import java.util.Collection;

@RequestMapping("customers")
@RestController
public class CustomersController {

    private final CustomersService customersService;

    public CustomersController(CustomersService customersService) {
        this.customersService = customersService;
    }

    @Operation(summary = "Добавление нового клиента",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Клиент добавлен",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class))
                    )
            }, tags = "Клиент")
    @PostMapping
    public ResponseEntity<Customers> createCustomer(@Parameter(description = "Chat id", example = "1") @RequestParam Long chatId,
                                           @Parameter(description = "Имя", example = "Сергей") @RequestParam String name,
                                           @Parameter(description = "Телефон", example = "+7-945-123-45-78") @RequestParam String phone) {
        Customers customer = customersService.createCustomer(new Customers(chatId,name,phone));
        return ResponseEntity.ok(customer);
    }

    @Operation(summary = "Поиск всех клиентов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Клиенты найдены",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class))
                    )
            }, tags = "Клиент")
    @GetMapping()
    public ResponseEntity<Collection<Customers>> getAllCustomers() {
        Collection<Customers> customers = customersService.getAllCustomers();
        if (customers == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Поиск клиента по id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Клиент найден",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Клиент не найден",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class))
                    )
            }, tags = "Клиент")
    @GetMapping("{customerId}")
    public ResponseEntity<Customers> getCustomerById(@Parameter(description = "id клиента", example = "1") @PathVariable Long customerId) {
        Customers customer = customersService.getCustomerById(customerId);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @Operation(summary = "Изменение данных клиента",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Клиент изменен",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class))
                    )
            }, tags = "Клиент")
    @PutMapping
    public ResponseEntity<Customers> updateCustomer(@Parameter(description = "id клиента", example = "1") @RequestParam Long customerId,
                                                    @Parameter(description = "Chat id", example = "1") @RequestParam Long chatId,
                                                    @Parameter(description = "Имя", example = "Сергей") @RequestParam String name,
                                                    @Parameter(description = "Телефон", example = "+7-945-123-45-78") @RequestParam String phone) {
        Customers customer = new Customers(chatId,name,phone);
        Customers updateCustomer = customersService.updateCustomer(customerId,customer);
        if (updateCustomer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateCustomer);
    }

    @Operation(summary = "Удаление клиента по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Клиент удален",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class))
                    )
            }, tags = "Клиент")
    @DeleteMapping("{customerId}")
    public ResponseEntity<Customers> deleteCustomer(@Parameter(description = "id клиента", example = "1") @PathVariable Long customerId) {
        customersService.deleteCustomer(customerId);
        return ResponseEntity.ok().build();
    }

}
