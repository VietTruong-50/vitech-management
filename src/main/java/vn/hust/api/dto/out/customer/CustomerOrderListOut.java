package vn.hust.api.dto.out.customer;

import lombok.Data;
import vn.hust.api.dto.out.order.OrderDetailOut;
import vn.hust.api.repository.helper.Col;

import java.util.List;

@Data
public class CustomerOrderListOut {

    @Col("order_id")
    private String orderId;
    @Col("order_code")
    private String orderCode;
    @Col("created_date")
    private String createdDate;
    @Col("total")
    private Long total;
    @Col("payment_method")
    private String paymentMethod;
    @Col("status")
    private String status;
    @Col("shipping_price")
    private Long shippingPrice;

    private List<OrderDetailOut> orderDetailOuts;


}
