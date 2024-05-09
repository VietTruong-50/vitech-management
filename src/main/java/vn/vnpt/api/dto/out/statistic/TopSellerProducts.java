package vn.vnpt.api.dto.out.statistic;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

@Data
public class TopSellerProducts {
    @Col("product_id")
    private String productId;
    @Col("product_code")
    private String productCode;
    @Col("name")
    private String name;
    @Col("actual_price")
    private Long actualPrice;
    @Col("total_sold")
    private Long totalSold;
}
