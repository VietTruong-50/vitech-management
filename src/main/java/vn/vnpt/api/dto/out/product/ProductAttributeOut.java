package vn.vnpt.api.dto.out.product;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

@Data
public class ProductAttributeOut {
    @Col("id")
    private String id;
    @Col("attribute_id")
    private String attributeId;
    @Col("name")
    private String name;
    @Col("data_type")
    private String dataType;
    @Col("value")
    private String value;
    @Col("price_add")
    private Long priceAdd;
}
