package vn.vnpt.api.service.helper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CategoryOut {
    private String categoryId;
    private String categoryName;
    @JsonIgnore
    private List<String> parentIds;
    private List<CategoryOut> children;

    public CategoryOut(String categoryId, String categoryName, List<String> parentIds) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentIds = parentIds;
        this.children = new ArrayList<>();
    }
}