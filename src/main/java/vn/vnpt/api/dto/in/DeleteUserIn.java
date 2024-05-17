package vn.vnpt.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteUserIn {
    @NotBlank
    private String userId;
}
