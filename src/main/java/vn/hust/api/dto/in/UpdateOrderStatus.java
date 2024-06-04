package vn.hust.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import vn.hust.api.enums.OrderStatusEnum;

@Data
public class UpdateOrderStatus {
    @NotBlank
    private String orderId;
    private OrderStatusEnum orderStatusEnum;
    private boolean confirmRefund;
}
