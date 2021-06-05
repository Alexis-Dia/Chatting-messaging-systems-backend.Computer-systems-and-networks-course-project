package alexeyd.com.controller;

import alexeyd.com.model.Report;
import alexeyd.com.repository.ReportRepository;
import alexeyd.com.service.CommonService;
import alexeyd.com.util.CryptoUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

@Controller
@AllArgsConstructor
public class ReportController {

  @Autowired
  private CommonService commonService;

    @Autowired
    private final ReportRepository reportRepository;

    @GetMapping(value = "/reports", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Report> getAll() {
        return reportRepository.findAllByCodeIsNot("1_000").map(
                report -> {
                    try {
                        report = CryptoUtils.decryptWholeObject(commonService.getSecretKey(), report);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return report;
                }
        );
    }

}
