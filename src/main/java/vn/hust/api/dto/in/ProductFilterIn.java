package vn.hust.api.dto.in;

import lombok.Data;
import vn.hust.api.enums.ProductStatusEnum;

import java.util.List;

@Data
public class ProductFilterIn {

    private String categoryId;
    private List<String> subCategories;
    private List<String> tags;
    private Long minPrice;
    private Long maxPrice;
    private ProductStatusEnum productStatusEnum;

    public String getSubCategoriesList() {
        return subCategories != null ? String.join(",", subCategories) : null;
    }

    public String getTagsList() {
        return tags != null ? String.join(",", tags) : null;
    }

}
