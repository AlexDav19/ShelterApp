package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.entity.Pets;
import pro.sky.telegrambot.service.PetsService;


import java.io.FileNotFoundException;
import java.util.Collection;

@RequestMapping("pets")
@RestController
public class PetsController {


    private final PetsService petsService;

    public PetsController(PetsService petsService) {
        this.petsService = petsService;
    }

    @Operation(summary = "Добавление нового питомца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Питомец добавлен",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Не найден файл с фото питомца",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class))
                    )
            }, tags = "Питомец")
    @PostMapping
    public ResponseEntity<Pets> createPets(@Parameter(description = "Имя питомца", example = "Шарик") @RequestParam String name,
                                           @Parameter(description = "Порода", example = "Дворняжка") @RequestParam String breed,
                                           @Parameter(description = "Возраст", example = "3") @RequestParam int age,
                                           @Parameter(description = "Название файла фото питомца", example = "Pet1.jpg") @RequestParam String Photo) throws FileNotFoundException {
        Pets createPets = petsService.createPets(new Pets(name, breed, age, "src/main/resources/pets/" + Photo));
        return ResponseEntity.ok(createPets);
    }

    @Operation(summary = "Поиск питомца по id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Питомец найден",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Питомец не найден",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class))
                    )
            }, tags = "Питомец")
    @GetMapping("{petId}")
    public ResponseEntity<Pets> getPetById(@Parameter(description = "id питомца", example = "1") @PathVariable Long petId) {
        Pets pet = petsService.getPetById(petId);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pet);
    }

    @Operation(summary = "Поиск всех питомцев",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Питомцы найдены",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class))
                    )
            }, tags = "Питомец")
    @GetMapping()
    public ResponseEntity<Collection<Pets>> getAllPet() {
        Collection<Pets> pets = petsService.getAllPet();
        if (pets == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pets);
    }

    @Operation(summary = "Изменение данных питомца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Питомец изменен",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Не найден файл с фото питомца",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class))
                    )
            }, tags = "Питомец")
    @PutMapping
    public ResponseEntity<Pets> updatePet(@Parameter(description = "id питомца", example = "1") @RequestParam Long petId,
                                          @Parameter(description = "Имя питомца", example = "Шарик") @RequestParam String name,
                                          @Parameter(description = "Порода", example = "Дворняжка") @RequestParam String breed,
                                          @Parameter(description = "Возраст", example = "3") @RequestParam int age,
                                          @Parameter(description = "Название файла фото питомца", example = "Pet1.jpg") @RequestParam String Photo) throws FileNotFoundException {
        Pets pet = new Pets(name, breed, age,"src/main/resources/pets/" + Photo);
        Pets updatePet = petsService.updatePet(petId, pet);
        if (updatePet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatePet);
    }

    @Operation(summary = "Удаление питомца по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Питомец удален",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class))
                    )
            }, tags = "Питомец")
    @DeleteMapping("{petId}")
        public ResponseEntity<Pets> deletePet(@Parameter(description = "id питомца", example = "1") @PathVariable Long petId) {
        petsService.deletePet(petId);
        return ResponseEntity.ok().build();
    }
}
