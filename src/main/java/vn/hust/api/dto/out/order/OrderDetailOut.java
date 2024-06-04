package vn.hust.api.dto.out.order;

import lombok.Data;
import vn.hust.api.repository.helper.Col;

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
    @Col("attribute")
    private String attribute;
}