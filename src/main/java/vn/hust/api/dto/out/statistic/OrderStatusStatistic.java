package vn.hust.api.dto.out.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderStatusStatistic {
    private Integer value;
    private String name;
}
