package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.entity.Shelters;
import pro.sky.telegrambot.service.SheltersService;

import java.util.Collection;
import java.util.List;

@RequestMapping("shelters")
@RestController
public class SheltersController {

    private final SheltersService sheltersService;

    public SheltersController(SheltersService sheltersService) {
        this.sheltersService = sheltersService;
    }

    @Operation(summary = "Добавление нового приюта",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Приют добавлен",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelters.class))
                    )
            }, tags = "Приюты")
    @PostMapping
    public ResponseEntity<Shelters> createStudent(@Parameter(description = "Адрес приюта", example = "г.Москва, ул.Гоголя 32") @RequestParam String address,
                                                  @Parameter(description = "График работы", example = "Часы работы: 09:00 - 18:00. Воскресенье - выходной") @RequestParam String workingHours,
                                                  @Parameter(description = "Название файла схемы проезда", example = "Schema.jpg") @RequestParam String drivingDirections) {
        Shelters createShelter = sheltersService.createShelter(new Shelters(address, workingHours, "src/main/resources/map/" + drivingDirections));
        return ResponseEntity.ok(createShelter);
    }

    @Operation(summary = "Поиск приюта по id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Приют найден",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelters.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Приют не найден",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelters.class))
                    )
            }, tags = "Приюты")
    @GetMapping("{shelterId}")
    public ResponseEntity<Shelters> getSheltersByAge(@Parameter(description = "id приюта", example = "1") @PathVariable Long shelterId) {
        Shelters shelter = sheltersService.getShelterById(shelterId);
        if (shelter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shelter);
    }

    @Operation(summary = "Поиск всех приютов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Приюты найдены",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelters.class))
                    )
            }, tags = "Приюты")
    @GetMapping()
    public ResponseEntity<Collection<Shelters>> getAllShelters() {
        Collection<Shelters> shelters = sheltersService.getAllShelter();
        if (shelters == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shelters);
    }

    @Operation(summary = "Изменение данных приюта",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Приют изменен",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelters.class))
                    )
            }, tags = "Приюты")
    @PutMapping
    public ResponseEntity<Shelters> updateStudent(@Parameter(description = "id приюта", example = "1") @RequestParam Long shelterId,
                                                  @Parameter(description = "Адрес приюта", example = "г.Москва, ул.Ленина 12") @RequestParam String address,
                                                  @Parameter(description = "График работы", example = "Часы работы: 10:00 - 18:00. Воскресенье - выходной") @RequestParam String workingHours,
                                                  @Parameter(description = "Название файла схемы проезда", example = "Schema1.jpg") @RequestParam String drivingDirections) {
        Shelters shelter = new Shelters(address, workingHours, "src/main/resources/map/" + drivingDirections);
        Shelters updateShelter = sheltersService.updateShelter(shelterId, shelter);
        if (updateShelter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateShelter);
    }

    @Operation(summary = "Удаление приюта по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Приют удален",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelters.class))
                    )
            }, tags = "Приюты")
    @DeleteMapping("{shelterId}")
    public ResponseEntity<Shelters> deleteStudent(@Parameter(description = "id приюта", example = "1") @PathVariable Long shelterId) {
        sheltersService.deleteShelter(shelterId);
        return ResponseEntity.ok().build();
    }
}
