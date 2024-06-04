package vn.hust.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryIn {
    @NotBlank
    private String categoryName;
    @NotBlank
    private String description;
    private String icon;
}
