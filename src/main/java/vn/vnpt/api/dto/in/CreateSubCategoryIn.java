package vn.vnpt.api.dto.in;

import lombok.Data;

@Data
public class CreateSubCategoryIn {
    private String subCategoryName;
    private String description;
    private String categoryId;
    private String icon;
}
