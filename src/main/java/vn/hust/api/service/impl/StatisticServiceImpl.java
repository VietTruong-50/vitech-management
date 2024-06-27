package vn.hust.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hust.api.dto.out.order.OrderListOut;
import vn.hust.api.dto.out.statistic.OrderStatusStatistic;
import vn.hust.api.dto.out.statistic.StatisticalData;
import vn.hust.api.dto.out.statistic.TopSellerProducts;
import vn.hust.api.enums.OrderStatusEnum;
import vn.hust.api.repository.OrderRepository;
import vn.hust.api.repository.StatisticRepository;
import vn.hust.api.service.StatisticService;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

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
    public List<TopSellerProducts> getProductReports(LocalDate startDate, LocalDate endDate, int type) {
        return statisticRepository.getTopSellerProducts(startDate, endDate, 5, type);
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
    public List<OrderStatusStatistic> getOrderStatus(Integer year) {
        List<OrderStatusStatistic> orderNumbers = new ArrayList<>();
        for (var it : OrderStatusEnum.values()) {
            if(!it.name().equals(OrderStatusEnum.ALL.name())){
                orderNumbers.add(new OrderStatusStatistic(statisticRepository.getOrderStatus(year, it.name()), it.name()));
            }
        }
        return orderNumbers;
    }

    @Override
    public Object getReportRevenue(LocalDate startDate, LocalDate endDate, int type) {
        return statisticRepository.getReportRevenue(startDate, endDate, type);
    }

}
