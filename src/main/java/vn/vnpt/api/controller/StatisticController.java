package vn.vnpt.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.service.StatisticService;
import vn.vnpt.common.AbstractResponseController;
import vn.vnpt.common.model.SortPageIn;

import java.time.LocalDate;
import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/v1/management/statistic", produces = "application/json")
public class StatisticController extends AbstractResponseController {
    private final StatisticService statisticService;

    @GetMapping(value = "/overview")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> getStatisticalData() {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /statistic/overview");
            var result = statisticService.statisticalData();
            log.info("[RESPONSE]: res: Success!");
            return result;
        });
    }

    @GetMapping(value = "/orders")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> statisticSuccessOrderAndOrderDateBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
                                                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
                                                                                      SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /statistic/orders");
            var result = statisticService.statisticSuccessOrder(startDate, endDate, sortPageIn);
            log.info("[RESPONSE]: res: Success!");
            return result;
        });
    }


    @GetMapping(value = "/top-sellers", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> getTop5Seller(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /statistic/top-sellers");
            var result = statisticService.top5seller(startDate, endDate);
            log.info("[RESPONSE]: res: Success!");
            return result;
        });
    }

    @GetMapping(value = "/yearly-statistics")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> getStatistics(@RequestParam Integer year) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /yearly-statistics");
            var revenue = statisticService.getRevenue(year);
            var orderStatus = statisticService.getOrderStatus(year);
            var statistics = new HashMap<>();
            statistics.put("revenue", revenue);
            statistics.put("order_status", orderStatus);
            log.info("[RESPONSE]: res: Success!");
            return statistics;
        });
    }

}
