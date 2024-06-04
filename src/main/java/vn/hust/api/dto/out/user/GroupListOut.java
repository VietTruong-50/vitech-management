package vn.hust.api.dto.out.user;

import lombok.Data;
import vn.hust.api.repository.helper.Col;

@Data
public class GroupListOut {
    @Col("id")
    private String id;
    @Col("name")
    private String name;
}
