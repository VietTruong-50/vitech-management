package vn.vnpt.api.service;

import vn.vnpt.api.dto.out.order.OrderListOut;
import vn.vnpt.api.dto.out.statistic.OrderStatusStatistic;
import vn.vnpt.api.dto.out.statistic.StatisticalData;
import vn.vnpt.api.dto.out.statistic.TopSellerProducts;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.time.LocalDate;
import java.util.List;

public interface StatisticService {
     StatisticalData statisticalData();

     PagingOut<OrderListOut> statisticSuccessOrder(LocalDate startDate, LocalDate endDate, SortPageIn sortPageIn);

     List<TopSellerProducts> top5seller(LocalDate startDate, LocalDate endDate);

     List<Long> getRevenue(Integer year);

     List<OrderStatusStatistic> getOrderStatus(Integer year);
}
