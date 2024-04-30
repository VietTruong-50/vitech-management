package vn.vnpt.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.vnpt.api.dto.out.order.OrderListOut;
import vn.vnpt.api.dto.out.statistic.StatisticalData;
import vn.vnpt.api.enums.OrderStatusEnum;
import vn.vnpt.api.repository.helper.ProcedureCallerV3;
import vn.vnpt.api.repository.helper.ProcedureParameter;
import vn.vnpt.common.Common;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class StatisticRepository {
    private final ProcedureCallerV3 procedureCallerV3;

    public StatisticalData statisticalData() {
        var outputs = procedureCallerV3.callNoRefCursor("statistical_data", List.of(
                ProcedureParameter.outputParam("out_product_numbers", Integer.class),
                ProcedureParameter.outputParam("out_order_numbers", Integer.class),
                ProcedureParameter.outputParam("out_customer_numbers", Integer.class),
                ProcedureParameter.outputParam("out_revenue", Long.class),
                ProcedureParameter.outputParam("out_result", String.class)
        ));

        if (!outputs.get("out_result").equals("success")) {
            throw new RuntimeException("call statistical_data failed");
        }

        int productNumbers = ((BigDecimal) outputs.get("out_product_numbers")).intValueExact();
        int customerNumbers = ((BigDecimal) outputs.get("out_customer_numbers")).intValueExact();
        int orderNumbers = ((BigDecimal) outputs.get("out_order_numbers")).intValueExact();
        long revenue = ((BigDecimal) outputs.get("out_revenue")).longValueExact();

        return StatisticalData.builder()
                .customerNumbers(customerNumbers)
                .productNumbers(productNumbers)
                .orderNumbers(orderNumbers)
                .revenue(revenue)
                .build();
    }

    public Long getRevenue(Integer year, Integer month) {
        var outputs = procedureCallerV3.callNoRefCursor("revenue_statistic", List.of(
                ProcedureParameter.inputParam("prs_year", Integer.class, year),
                ProcedureParameter.inputParam("prs_month", Integer.class, month),
                ProcedureParameter.outputParam("out_result", String.class),
                ProcedureParameter.outputParam("out_month", BigDecimal.class)
        ));

        BigDecimal revenue = (BigDecimal) outputs.get("out_month");
        return !Common.isNullOrEmpty(revenue) ? revenue.longValueExact() : 0L;
    }


    public Integer getOrderStatus(Integer year, String status) {
        var outputs = procedureCallerV3.callNoRefCursor("statistical_order_data", List.of(
                ProcedureParameter.inputParam("prs_status", String.class, status),
                ProcedureParameter.outputParam("out_result", String.class),
                ProcedureParameter.outputParam("out_number_order", BigDecimal.class)
        ));

        BigDecimal orderNumber = (BigDecimal) outputs.get("out_number_order");
        return !Common.isNullOrEmpty(orderNumber) ? orderNumber.intValueExact() : 0;
    }
}
