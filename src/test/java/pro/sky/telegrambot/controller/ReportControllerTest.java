package pro.sky.telegrambot.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import pro.sky.telegrambot.entity.Report;

import java.util.Collection;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    ReportController reportController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(reportController).isNotNull();
    }

    @Test
    void getReportByIdTest_success() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/report/" + 0L
                        , Report.class))
                .isNotNull();
    }

    @Test
    void getPhotoReportByIdTest_success() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/report/photo/" + 0L
                        , Report.class))
                .isNotNull();
    }

    @Test
    public void getNewReportTest_success(){
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/report", Collection.class))
                .isNotNull();

    }

    @Test
    public void sendMessageAboutBadReportTest_success(){
        Report report = new Report();
        Assertions
                .assertThat(this.restTemplate.patchForObject("http://localhost:" + port + "/report/" + 0L, report
                        , Report.class))
                .isNotNull();

    }
}
