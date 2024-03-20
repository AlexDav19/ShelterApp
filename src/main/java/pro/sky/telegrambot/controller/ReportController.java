package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.entity.Report;
import pro.sky.telegrambot.service.ReportService;

import java.util.Collection;

@RequestMapping("report")
@RestController
public class ReportController {


    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Поиск отчета по id и помечает его отсмотренным.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет найден",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Отчет не найден",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class))
                    )
            }, tags = "Отчет")
    @GetMapping("{reportId}")
    public ResponseEntity<Report> getReportById(@Parameter(description = "id отчета", example = "1") @PathVariable Long reportId) {
        Report report = reportService.getReportById(reportId);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }

    @Operation(summary = "Поиск новых отчетов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчеты найдены",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class))
                    )
            }, tags = "Отчет")
    @GetMapping()
    public ResponseEntity<Collection<Report>> getNewReport() {
        Collection<Report> report = reportService.getNewReport();
        if (report == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }

    @Operation(summary = "Отправка сообщения о ненадлежащем сданном отчете.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Сообщение отправлено",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class))
                    )
            }, tags = "Отчет")
    @PatchMapping("{badReportId}")
    public ResponseEntity<Report> sendMessageAboutBadReport(@Parameter(description = "id отчета", example = "1") @PathVariable Long badReportId) {

        Report report = reportService.sendMessageAboutBadReport(badReportId);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }

}
