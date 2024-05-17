package vn.vnpt.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateSubCategoryIn {
    @NotBlank
    private String subCategoryName;
    private String description;
    private String icon;
}
