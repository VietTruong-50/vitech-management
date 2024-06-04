package vn.hust.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.hust.api.dto.in.UpdateOrderStatus;
import vn.hust.api.dto.out.order.OrderInformationOut;
import vn.hust.api.dto.out.order.OrderListOut;
import vn.hust.api.enums.OrderStatusEnum;
import vn.hust.api.repository.OrderRepository;
import vn.hust.api.service.OrderService;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderInformationOut getOrderDetail(String orderId) {
        return orderRepository.getOrderDetail(orderId);
    }

    @Override
    public PagingOut<OrderListOut> getAllOrders(OrderStatusEnum status, SortPageIn sortPageIn) {
        var page = orderRepository.listOrders(null, null, status, sortPageIn);

        for (var it : page.getData()) {
            it.setOrderDetailOuts(orderRepository.getOrderDetailList(it.getOrderCode()));
        }
        return page;
    }

    @Override
    public void updateOrderStatus(UpdateOrderStatus updateOrderStatus) {
        orderRepository.updateOrderStatus(updateOrderStatus);
    }

    @Override
    public void destroyOrder(String orderId) {
        orderRepository.destroyOrder(orderId);
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void autoUpdateShippedOrder(){
        orderRepository.autoUpdateShippedOrder();
    }
}
