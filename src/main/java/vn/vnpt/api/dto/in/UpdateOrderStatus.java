package vn.vnpt.api.dto.in;

import lombok.Data;
import vn.vnpt.api.enums.OrderStatusEnum;

@Data
public class UpdateOrderStatus {
    private String orderId;
    private OrderStatusEnum orderStatusEnum;
}
