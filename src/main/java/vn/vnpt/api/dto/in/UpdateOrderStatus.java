package vn.vnpt.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import vn.vnpt.api.enums.OrderStatusEnum;

@Data
public class UpdateOrderStatus {
    @NotBlank
    private String orderId;
    private OrderStatusEnum orderStatusEnum;
}
