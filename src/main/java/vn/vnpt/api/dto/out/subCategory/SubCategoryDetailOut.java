package vn.vnpt.api.dto.out.subCategory;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

@Data
public class SubCategoryDetailOut {
    @Col("subcategory_id")
    private String subCategoryId;
    @Col("subcategory_name")
    private String name;
    @Col("description")
    private String description;
    @Col("icon")
    private String icon;
}
