package vn.hust.api.dto.out.product.attribute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import vn.hust.api.repository.helper.Col;

@Data
public class AttributeListOut {
    @Col(value = "id")
    private String attributeId;
    @Col(value = "name")
    private String name;
    @Col(value = "data_type")
    private String type;
    @JsonIgnore
    @Col(value = "values")
    private String values;

    private Object valueList;
}
