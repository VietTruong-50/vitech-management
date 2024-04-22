package vn.vnpt.api.service;

import vn.vnpt.api.dto.out.customer.CustomerDetailOut;
import vn.vnpt.api.dto.out.customer.CustomerListOut;
import vn.vnpt.api.dto.out.customer.CustomerOrderListOut;
import vn.vnpt.api.enums.OrderStatusEnum;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.List;

public interface CustomerService {
    PagingOut<CustomerListOut> findAllCustomers(SortPageIn sortPageIn);

    CustomerDetailOut getCustomerDetail(String customerId);

    List<CustomerOrderListOut> getCustomerOrderList( String customerId, OrderStatusEnum statusEnum, SortPageIn sortPageIn);
}
