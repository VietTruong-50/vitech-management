package vn.vnpt.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreateCategoryIn {
    @NotBlank
    private String categoryName;
    @NotBlank
    private String description;
    private String icon;
}
