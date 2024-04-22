package vn.vnpt.api.dto.in;

import lombok.Data;
import vn.vnpt.api.enums.ProductStatusEnum;

import java.util.List;
import java.util.Map;

@Data
public class CreateProductIn {
    private String name;
    private String productCode;
    private String description;
    private String categoryId;
    private String subcategoryId;
    private Integer quantity;
    private String summary;
    private List<String> tagIds;
    private Long price;
    private Long actualPrice;
    private ProductStatusEnum status;
    private Map<String, Object> parameters;
    private Map<String, List<String>> attributes;
}
