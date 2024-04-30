package vn.vnpt.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.out.order.OrderListOut;
import vn.vnpt.api.dto.out.statistic.StatisticalData;
import vn.vnpt.api.enums.OrderStatusEnum;
import vn.vnpt.api.repository.OrderRepository;
import vn.vnpt.api.repository.StatisticRepository;
import vn.vnpt.api.service.StatisticService;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository statisticRepository;
    private final OrderRepository orderRepository;

    @Override
    public StatisticalData statisticalData() {
        return statisticRepository.statisticalData();
    }

    @Override
    public PagingOut<OrderListOut> statisticSuccessOrder(LocalDate startDate, LocalDate endDate, SortPageIn sortPageIn) {
        var page = orderRepository.listOrders(startDate, endDate, OrderStatusEnum.COMPLETED, sortPageIn);
        for (var it : page.getData()) {
            it.setOrderDetailOuts(orderRepository.getOrderDetailList(it.getOrderCode()));
        }
        return page;
    }

    @Override
    public Object top5seller(LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<Long> getRevenue(Integer year) {
        List<Long> revenues = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            revenues.add(statisticRepository.getRevenue(year, i));
        }
        return revenues;
    }

    @Override
    public List<Integer> getOrderStatus(Integer year) {
        List<Integer> orderNumbers = new ArrayList<>();
        for (var it : OrderStatusEnum.values()) {
            if(!it.name().equals(OrderStatusEnum.ALL.name())){
                orderNumbers.add(statisticRepository.getOrderStatus(year, it.name()));
            }
        }
        return orderNumbers;
    }
}
