package vn.hust.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteUserGroupIn {
    @NotBlank
    private String userId;
    @NotBlank
    private String groupId;
}
