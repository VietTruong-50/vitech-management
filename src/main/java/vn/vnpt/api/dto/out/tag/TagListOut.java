package vn.vnpt.api.dto.out.tag;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

@Data
public class TagListOut {
    @Col("tag_id")
    private String tagId;
    @Col("tag_name")
    private String tagName;
}
