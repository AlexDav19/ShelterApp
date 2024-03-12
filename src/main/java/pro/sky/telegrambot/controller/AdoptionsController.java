package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.entity.Adoptions;
import pro.sky.telegrambot.service.AdoptionsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@RequestMapping("adoptions")
@RestController
public class AdoptionsController {



    private final AdoptionsService adoptionsService;

    public AdoptionsController(AdoptionsService adoptionsService) {
        this.adoptionsService = adoptionsService;
    }

    @Operation(summary = "Добавление нового усыновителя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновитель добавлен",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoptions.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "В базе нет id пользователя или питомца",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoptions.class))
                    )
            }, tags = "Усыновитель")
    @PostMapping
    public ResponseEntity<Adoptions> createAdoption(@Parameter(description = "Id клиента", example = "1") @RequestParam Long customerId,
                                           @Parameter(description = "Id питомца", example = "1") @RequestParam Long petId,
                                           @Parameter(description = "Дата окончания испытательного срока", example = "2014-04-08 21:00") @RequestParam String trialEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(trialEnd, formatter);
        Adoptions createAdoption = adoptionsService.createAdoptions(new Adoptions(customerId, petId, dateTime));
        return ResponseEntity.ok(createAdoption);
    }

    @Operation(summary = "Поиск усыновителя по id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновитель найден",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoptions.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Усыновитель не найден",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoptions.class))
                    )
            }, tags = "Усыновитель")
    @GetMapping("{adoptionId}")
    public ResponseEntity<Adoptions> getAdoptionById(@Parameter(description = "id усыновителя", example = "1") @PathVariable Long adoptionId) {
        Adoptions adoption = adoptionsService.getAdoptionsById(adoptionId);
        if (adoption == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adoption);
    }

    @Operation(summary = "Поиск всех усыновителей",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновители найдены",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoptions.class))
                    )
            }, tags = "Усыновитель")
    @GetMapping()
    public ResponseEntity<Collection<Adoptions>> getAllAdoptions() {
        Collection<Adoptions> adoptions = adoptionsService.getAllAdoptions();
        if (adoptions == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adoptions);
    }

    @Operation(summary = "Изменение данных усыновителя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновитель изменен",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoptions.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "В базе нет id пользователя или питомца",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoptions.class))
                    )
            }, tags = "Усыновитель")
    @PutMapping
    public ResponseEntity<Adoptions> updateAdoption(@Parameter(description = "Id усыновителя", example = "1") @RequestParam Long adoptionId,
                                                    @Parameter(description = "Id клиента", example = "1") @RequestParam Long customerId,
                                                    @Parameter(description = "Id питомца", example = "1") @RequestParam Long petId,
                                                    @Parameter(description = "Дата окончания испытательного срока", example = "2024-04-08") @RequestParam LocalDateTime trialEnd)  {
        Adoptions adoption = new Adoptions(customerId, petId, trialEnd);
        Adoptions updateAdoption = adoptionsService.updateAdoptions(adoptionId, adoption);
        if (updateAdoption == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateAdoption);
    }

    @Operation(summary = "Удаление усыновителя по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновитель удален",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoptions.class))
                    )
            }, tags = "Усыновитель")
    @DeleteMapping("{adoptionId}")
    public ResponseEntity<Adoptions> deleteAdoption(@Parameter(description = "id усыновителя", example = "1") @PathVariable Long adoptionId) {
        adoptionsService.deleteAdoptions(adoptionId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Продлить испытательный срок на 14 дней",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Срок продлен на 14 дней",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoptions.class))
                    )
            }, tags = "Усыновитель")
    @PutMapping("{adoptionId14Days}")
    public ResponseEntity<Adoptions> trialEndPlus14Days(@Parameter(description = "id усыновителя", example = "1") @PathVariable Long adoptionId14Days) {
        Adoptions updateAdoption =  adoptionsService.trialEndPlus14Days(adoptionId14Days);
        if (updateAdoption == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateAdoption);
    }

    @Operation(summary = "Продлить испытательный срок на 30 дней",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Срок продлен на 30 дней",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoptions.class))
                    )
            }, tags = "Усыновитель")
    @PutMapping("{adoptionId30Days}")
    public ResponseEntity<Adoptions> trialEndPlus30Days(@Parameter(description = "id усыновителя", example = "1") @PathVariable Long adoptionId30Days) {
        Adoptions updateAdoption =  adoptionsService.trialEndPlus30Days(adoptionId30Days);
        if (updateAdoption == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateAdoption);
    }

}
