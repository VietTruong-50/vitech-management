package vn.vnpt.api.service;

import vn.vnpt.api.dto.out.order.OrderListOut;
import vn.vnpt.api.dto.out.statistic.StatisticalData;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.time.LocalDate;
import java.util.List;

public interface StatisticService {
     StatisticalData statisticalData();

     PagingOut<OrderListOut> statisticSuccessOrder(LocalDate startDate, LocalDate endDate, SortPageIn sortPageIn);

     Object top5seller(LocalDate startDate, LocalDate endDate);

     List<Long> getRevenue(Integer year);

     List<Integer> getOrderStatus(Integer year);
}
