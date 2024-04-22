package vn.vnpt.api.dto.in;

import lombok.Data;

@Data
public class UpdateSubCategoryIn {
    private String subCategoryName;
    private String description;
    private String icon;
}
