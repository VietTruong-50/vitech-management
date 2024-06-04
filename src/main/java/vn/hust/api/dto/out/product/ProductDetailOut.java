package vn.hust.api.dto.out.product;

import lombok.Data;
import vn.hust.api.repository.helper.Col;

import java.util.List;
import java.util.Map;

@Data
public class ProductDetailOut {
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
    @Col("price")
    private Long price;
    @Col("actual_price")
    private Long actualPrice;
    @Col("quantity")
    private Integer quantity;
    @Col("feature_image_link")
    private String featureImageLink;
    @Col("subcategory_id")
    private String subcategoryId;
    @Col("category_id")
    private String categoryId;
    @Col("parameters")
    private String parametersJson;
    @Col("status")
    private Integer status;
    @Col("image_links")
    private String imageLinks;

    private Map<String, Object> parameters;

    private Map<String, List<ProductAttributeOut>> attributes;

    private List<TagProduct> tagProducts;

    @Data
    public static class TagProduct {
        @Col("tag_name")
        private String display;
        @Col("tag_id")
        private String value;
    }
}

