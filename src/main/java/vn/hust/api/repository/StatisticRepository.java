package vn.hust.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.hust.api.dto.out.statistic.StatisticalData;
import vn.hust.api.dto.out.statistic.TopSellerProducts;
import vn.hust.api.repository.helper.ProcedureCallerV3;
import vn.hust.api.repository.helper.ProcedureParameter;
import vn.hust.common.Common;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
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

        int productNumbers = !Common.isNullOrEmpty(outputs.get("out_product_numbers")) ? ((BigDecimal) outputs.get("out_product_numbers")).intValueExact() : 0;
        int customerNumbers = !Common.isNullOrEmpty(outputs.get("out_customer_numbers")) ? ((BigDecimal) outputs.get("out_customer_numbers")).intValueExact() : 0;
        int orderNumbers = !Common.isNullOrEmpty(outputs.get("out_order_numbers")) ? ((BigDecimal) outputs.get("out_order_numbers")).intValueExact() : 0;
        long revenue = !Common.isNullOrEmpty(outputs.get("out_revenue")) ? ((BigDecimal) outputs.get("out_revenue")).longValueExact() : 0;

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

    public List<TopSellerProducts> getTopSellerProducts(LocalDate startDate, LocalDate endDate, int limit, int type) {
        var outputs = procedureCallerV3.callOneRefCursor("get_product_reports", List.of(
                ProcedureParameter.inputParam("prs_create_date_from", String.class, !Common.isNullOrEmpty(startDate) ? startDate.toString() : null),
                ProcedureParameter.inputParam("prs_create_date_to", String.class, !Common.isNullOrEmpty(endDate) ? endDate.toString() : null),
                ProcedureParameter.inputParam("prs_limit", Integer.class, limit),
                ProcedureParameter.inputParam("prs_type", Integer.class, type),
                ProcedureParameter.refCursorParam("out_cur")
        ), TopSellerProducts.class);
        return (List<TopSellerProducts>) outputs.get("out_cur");
    }
}
