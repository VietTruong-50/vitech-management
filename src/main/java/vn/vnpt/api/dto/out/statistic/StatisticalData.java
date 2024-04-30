package vn.vnpt.api.dto.out.statistic;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticalData {
    private Integer productNumbers;
    private Integer orderNumbers;
    private Integer customerNumbers;
    private Long revenue;
}
