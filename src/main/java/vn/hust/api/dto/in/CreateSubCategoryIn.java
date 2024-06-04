package vn.hust.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSubCategoryIn {
    @NotBlank
    private String subCategoryName;
    private String description;
    @NotBlank
    private String categoryId;
    private String icon;
}
