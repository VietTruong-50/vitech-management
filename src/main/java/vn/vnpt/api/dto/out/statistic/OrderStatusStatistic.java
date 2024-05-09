package vn.vnpt.api.dto.out.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import vn.vnpt.api.enums.OrderStatusEnum;

@Data
@AllArgsConstructor
public class OrderStatusStatistic {
    private Integer value;
    private String name;
}
