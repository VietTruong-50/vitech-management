package vn.vnpt.api.dto.out.product;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

@Data
public class ProductListOut {
    @Col("product_id")
    private String id;
    @Col("name")
    private String name;
    @Col("product_code")
    private String productCode;
    @Col("description")
    private String description;
    @Col("summary")
    private String summary;
    @Col("feature_image_link")
    private String featureImageLink;
    @Col("price")
    private Long price;
    @Col("actual_price")
    private Long actualPrice;
    @Col("quantity")
    private Integer quantity;
    @Col("status")
    private Integer status;
    @Col("subcategory_name")
    private String subCategoryName;
    @Col("category_name")
    private String categoryName;
    @Col("created_date")
    private String createdDate;
    @Col("updated_date")
    private String updatedDate;
}
