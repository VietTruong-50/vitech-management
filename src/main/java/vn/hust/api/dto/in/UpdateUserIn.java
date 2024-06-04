package vn.hust.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserIn {
    @NotBlank
    private String userId;
    @NotBlank
    private String userName;
    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private List<String> groupsToAdd;
    private List<String> groupsToRemove;
}
