package vn.hust.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderDetailIn {
    @NotBlank
    private String orderId;
}
