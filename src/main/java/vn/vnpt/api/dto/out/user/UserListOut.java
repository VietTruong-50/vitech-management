package vn.vnpt.api.dto.out.user;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

@Data
public class UserListOut {
    @Col("id")
    private String id;
    @Col("email")
    private String email;
    @Col("first_name")
    private String firstName;
    @Col("last_name")
    private String lastName;
    @Col("username")
    private String userName;
}
