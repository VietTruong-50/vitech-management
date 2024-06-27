package vn.hust.api.dto.out.statistic;

import lombok.Data;
import vn.hust.api.repository.helper.Col;

@Data
public class ReportStatisticOut {
    @Col(value = "name")
    private String name;
    @Col(value = "quantity")
    private Integer quantity;
    @Col(value = "total_revenue")
    private Long total;
}
