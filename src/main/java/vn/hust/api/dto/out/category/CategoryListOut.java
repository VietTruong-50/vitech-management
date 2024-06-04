package vn.hust.api.dto.out.category;

import lombok.Data;
import vn.hust.api.repository.helper.Col;

@Data
public class CategoryListOut {
    @Col("category_id")
    private String categoryId;
    @Col("category_name")
    private String categoryName;
    @Col("icon")
    private String icon;
}
