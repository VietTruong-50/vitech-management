package vn.hust.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import vn.hust.api.enums.ProductStatusEnum;

import java.util.List;
import java.util.Map;

@Data
public class CreateProductIn {
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
    @NotBlank
    private String summary;
    private List<String> tagIds;
    private Long price;
    @NotNull
    private Long actualPrice;
    private ProductStatusEnum status;
    private Map<String, Object> parameters;
    private Map<String, List<String>> attributes;
}
