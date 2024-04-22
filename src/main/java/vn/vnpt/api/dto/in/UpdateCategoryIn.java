package vn.vnpt.api.dto.in;

import lombok.Data;

@Data
public class UpdateCategoryIn {
    private String categoryId;
    private String categoryName;
    private String description;
    private String imageUrl;
}
