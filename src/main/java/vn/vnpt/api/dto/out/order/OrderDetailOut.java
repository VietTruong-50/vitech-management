package vn.vnpt.api.dto.out.order;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

@Data
public class OrderDetailOut {
    @Col("product_id")
    private String productId;
    @Col("product_code")
    private String productCode;
    @Col("name")
    private String name;
    @Col("item_price")
    private Long price;
    @Col("quantity")
    private Integer quantity;
}