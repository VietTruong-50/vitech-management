package vn.vnpt.api.service;

import vn.vnpt.api.dto.in.UpdateOrderStatus;
import vn.vnpt.api.dto.out.order.OrderInformationOut;
import vn.vnpt.api.dto.out.order.OrderListOut;
import vn.vnpt.api.enums.OrderStatusEnum;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

public interface OrderService {
    OrderInformationOut getOrderDetail(String orderId);

    PagingOut<OrderListOut> getAllOrders(OrderStatusEnum status, SortPageIn sortPageIn);

    void updateOrderStatus(UpdateOrderStatus updateOrderStatus);

    void destroyOrder(String orderId);
}
