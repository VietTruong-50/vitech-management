package vn.vnpt.api.dto.in;

import lombok.Data;

import java.util.List;

@Data
public class CreateUserIn {
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> groupIds;
}
