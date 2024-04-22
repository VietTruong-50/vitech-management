package vn.vnpt.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.out.customer.CustomerDetailOut;
import vn.vnpt.api.dto.out.customer.CustomerListOut;
import vn.vnpt.api.dto.out.customer.CustomerOrderListOut;
import vn.vnpt.api.enums.OrderStatusEnum;
import vn.vnpt.api.repository.CustomerRepository;
import vn.vnpt.api.repository.OrderRepository;
import vn.vnpt.api.service.CustomerService;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Override
    public PagingOut<CustomerListOut> findAllCustomers(SortPageIn sortPageIn) {
        return customerRepository.findAllCustomers(sortPageIn);
    }

    @Override
    public CustomerDetailOut getCustomerDetail(String customerId) {
        return customerRepository.getCustomerDetail(customerId);
    }

    @Override
    public List<CustomerOrderListOut> getCustomerOrderList(String customerId, OrderStatusEnum statusEnum, SortPageIn sortPageIn){
        var rs = orderRepository.getOrderByCustomerByStatus(customerId, statusEnum, sortPageIn);

        for(var it : rs){
            it.setOrderDetailOuts(orderRepository.getOrderDetailList(it.getOrderCode()));
        }

        return rs;
    }
}
