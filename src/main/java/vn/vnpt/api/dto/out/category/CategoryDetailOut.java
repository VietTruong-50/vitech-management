package vn.vnpt.api.dto.out.category;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

@Data
public class CategoryDetailOut {
    @Col("category_id")
    private String categoryId;
    @Col("category_name")
    private String categoryName;
    @Col("description")
    private String description;
    @Col("image_url")
    private String imageUrl;
    @Col("user_created")
    private String userCreated;
    @Col("created_date")
    private String createdDate;
    @Col("attribute_ids")
    private String attributeIds;
}
