package vn.vnpt.api.dto.in;

import lombok.Data;

import java.util.List;

@Data
public class CreateCategoryIn {
    private String categoryName;
    private String description;
    private String icon;
}
