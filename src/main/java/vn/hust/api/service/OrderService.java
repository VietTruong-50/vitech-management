package vn.hust.api.service;

import vn.hust.api.dto.in.UpdateOrderStatus;
import vn.hust.api.dto.out.order.OrderInformationOut;
import vn.hust.api.dto.out.order.OrderListOut;
import vn.hust.api.enums.OrderStatusEnum;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

public interface OrderService {
    OrderInformationOut getOrderDetail(String orderId);

    PagingOut<OrderListOut> getAllOrders(OrderStatusEnum status, SortPageIn sortPageIn);

    void updateOrderStatus(UpdateOrderStatus updateOrderStatus);

    void destroyOrder(String orderId);
}
