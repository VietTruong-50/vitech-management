package vn.vnpt.api.dto.out.order;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;


@Data
public class OrderInformationOut {
    @Col("order_id")
    private String orderId;

    @Col("invoice_symbol")
    private String invoiceSymbol;

    @Col("tax_number")
    private String taxNumber;

    @Col("tax_authorities_code")
    private String taxAuthoritiesCode;

    @Col("order_code")
    private String orderCode;

    @Col("total")
    private double total;

    @Col("status")
    private int status;

    @Col("delivery_date")
    private String deliveryString;

    @Col("shipping_method_id")
    private String shippingMethodId;

    @Col("card_payment_id")
    private String cardPaymentId;

    @Col("payment_method")
    private String paymentMethod;

    @Col("address_id")
    private String addressId;

    @Col("user_created")
    private String userCreated;

    @Col("created_date")
    private String createdDate;

    @Col("updated_date")
    private String updatedDate;

    @Col("cancel_date")
    private String cancelDate;

    @Col("success_date")
    private String successDate;

    @Col("refund_date")
    private String refundDate;

    @Col("confirm_date")
    private String confirmDate;
}
