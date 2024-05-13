package vn.vnpt.api.dto.out.user;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

@Data
public class GroupListOut {
    @Col("id")
    private String id;
    @Col("name")
    private String name;
}
