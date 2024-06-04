package vn.hust.api.service;

import vn.hust.api.dto.out.customer.CustomerDetailOut;
import vn.hust.api.dto.out.customer.CustomerListOut;
import vn.hust.api.dto.out.customer.CustomerOrderListOut;
import vn.hust.api.enums.OrderStatusEnum;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.util.List;

public interface CustomerService {
    PagingOut<CustomerListOut> findAllCustomers(SortPageIn sortPageIn);

    CustomerDetailOut getCustomerDetail(String customerId);

    List<CustomerOrderListOut> getCustomerOrderList( String customerId, OrderStatusEnum statusEnum, SortPageIn sortPageIn);
}
