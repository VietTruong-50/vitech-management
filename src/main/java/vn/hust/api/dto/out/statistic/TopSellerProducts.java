package vn.hust.api.dto.out.statistic;

import lombok.Data;
import vn.hust.api.repository.helper.Col;

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
    @Col("activity_count")
    private Long totalView;
}
