package vn.vnpt.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.in.UpdateOrderStatus;
import vn.vnpt.api.dto.out.order.OrderInformationOut;
import vn.vnpt.api.dto.out.order.OrderListOut;
import vn.vnpt.api.enums.OrderStatusEnum;
import vn.vnpt.api.repository.OrderRepository;
import vn.vnpt.api.service.OrderService;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
