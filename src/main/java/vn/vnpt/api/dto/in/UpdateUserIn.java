package vn.vnpt.api.dto.in;

import lombok.Data;

import java.util.List;

@Data
public class UpdateUserIn {
    private String userId;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> groupsToAdd;
    private List<String> groupsToRemove;
}
