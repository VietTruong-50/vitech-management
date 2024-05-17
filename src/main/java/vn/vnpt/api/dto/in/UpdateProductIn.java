package vn.vnpt.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import vn.vnpt.api.enums.ProductStatusEnum;

import java.util.List;
import java.util.Map;

@Data
public class UpdateProductIn {
    @NotBlank
    private String productId;
    @NotBlank
    private String name;
    @NotBlank
    private String productCode;
    @NotBlank
    private String description;
    @NotBlank
    private String categoryId;
    @NotBlank
    private String subcategoryId;
    @NotNull
    private Integer quantity;
    private String summary;
    private List<String> tagIds;
    @NotNull
    private Long price;
    @NotNull
    private Long actualPrice;
    private List<String> imageDelete;
    private ProductStatusEnum status;
    private Map<String, Object> parameters;
    private Map<String, List<String>> attributes;
}
