package vn.hust.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCategoryIn {
    @NotBlank
    private String categoryId;
    @NotBlank
    private String categoryName;
    private String description;
    private String imageUrl;
}
