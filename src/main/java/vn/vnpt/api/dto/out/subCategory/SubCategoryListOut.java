package vn.vnpt.api.dto.out.subCategory;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

@Data
public class SubCategoryListOut {
    @Col("subcategory_id")
    private String subCategoryId;
    @Col("category_id")
    private String categoryId;
    @Col("category_name")
    private String categoryName;
    @Col("subcategory_name")
    private String name;
    @Col("icon")
    private String icon;
}
