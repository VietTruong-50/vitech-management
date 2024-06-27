package vn.hust.api.service;

import vn.hust.api.dto.out.order.OrderListOut;
import vn.hust.api.dto.out.statistic.OrderStatusStatistic;
import vn.hust.api.dto.out.statistic.StatisticalData;
import vn.hust.api.dto.out.statistic.TopSellerProducts;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.time.LocalDate;
import java.util.List;

public interface StatisticService {
     StatisticalData statisticalData();

     PagingOut<OrderListOut> statisticSuccessOrder(LocalDate startDate, LocalDate endDate, SortPageIn sortPageIn);

     List<TopSellerProducts> getProductReports(LocalDate startDate, LocalDate endDate, int type);

     List<Long> getRevenue(Integer year);

     List<OrderStatusStatistic> getOrderStatus(Integer year);

     Object getReportRevenue(LocalDate startDate, LocalDate endDate, int type);
}
