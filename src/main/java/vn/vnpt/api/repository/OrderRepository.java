package vn.vnpt.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.vnpt.api.dto.in.UpdateOrderStatus;
import vn.vnpt.api.dto.out.customer.CustomerOrderListOut;
import vn.vnpt.api.dto.out.order.OrderDetailOut;
import vn.vnpt.api.dto.out.order.OrderInformationOut;
import vn.vnpt.api.dto.out.order.OrderListOut;
import vn.vnpt.api.enums.OrderStatusEnum;
import vn.vnpt.api.repository.helper.ProcedureCallerV3;
import vn.vnpt.api.repository.helper.ProcedureParameter;
import vn.vnpt.common.Common;
import vn.vnpt.common.constant.DatabaseStatus;
import vn.vnpt.common.exception.ApiErrorException;
import vn.vnpt.common.exception.NotFoundException;
import vn.vnpt.common.exception.model.ApiError;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class OrderRepository {
    private final ProcedureCallerV3 procedureCallerV3;

    public OrderInformationOut getOrderDetail(String orderCode) {
        var outputs = procedureCallerV3.callOneRefCursor("order_detail_by_code",
                List.of(
                        ProcedureParameter.inputParam("prs_order_code", String.class, orderCode),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ),
                OrderInformationOut.class
        );
        List<OrderInformationOut> outList = (List<OrderInformationOut>) outputs.get("out_cur");
        if (outList == null || outList.isEmpty()) {
            throw new NotFoundException("call order_code_detail failed!");
        }

        return outList.get(0);
    }

    public PagingOut<OrderListOut> listOrders(LocalDate fromDate, LocalDate toDate, OrderStatusEnum status, SortPageIn sortPageIn) {
        if (status.equals(OrderStatusEnum.ALL)) {
            status = null;
        }

        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("order_list_filter",
                List.of(
                        ProcedureParameter.inputParam("prs_customer_id", String.class, null),
                        ProcedureParameter.inputParam("prs_status", String.class, status),
                        ProcedureParameter.inputParam("prs_properties_sort", String.class, sortPageIn.getPropertiesSort()),
                        ProcedureParameter.inputParam("prs_sort", String.class, sortPageIn.getSort()),
                        ProcedureParameter.inputParam("prn_page_index", Integer.class, sortPageIn.getPage()),
                        ProcedureParameter.inputParam("prn_page_size", Integer.class, sortPageIn.getMaxSize()),
                        ProcedureParameter.inputParam("prs_key_search", String.class, sortPageIn.getKeySearch()),
                        ProcedureParameter.inputParam("prs_create_date_from", String.class, !Common.isNullOrEmpty(fromDate) ? fromDate.toString() : null),
                        ProcedureParameter.inputParam("prs_create_date_to", String.class, !Common.isNullOrEmpty(toDate) ? toDate.toString() : null),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), OrderListOut.class
        );

        var outList = (List<OrderListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

    public void updateOrderStatus(UpdateOrderStatus updateOrderStatus) {
        var outputs = procedureCallerV3.callNoRefCursor("order_update_status", List.of(
                ProcedureParameter.inputParam("prs_order_id", String.class, updateOrderStatus.getOrderId()),
                ProcedureParameter.inputParam("prs_status", Integer.class, updateOrderStatus.getOrderStatusEnum().value),
                ProcedureParameter.inputParam("prs_confirm_refund", Boolean.class, updateOrderStatus.isConfirmRefund()),
                ProcedureParameter.outputParam("out_result", String.class))
        );
        String result = (String) outputs.get("out_result");
        if (DatabaseStatus.isExist(result)) throw new ApiErrorException(ApiError.fieldExist(result));
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("order_update_status failed!");
    }

    public void autoUpdateShippedOrder() {
        var outputs = procedureCallerV3.callNoRefCursor("order_auto_update_status",
                List.of(ProcedureParameter.outputParam("out_result", String.class))
        );
        String result = (String) outputs.get("out_result");
        if (DatabaseStatus.isExist(result)) throw new ApiErrorException(ApiError.fieldExist(result));
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("order_auto_update_status failed!");
    }

    public void destroyOrder(String orderId) {
        var outputs = procedureCallerV3.callNoRefCursor("order_delete", List.of(
                ProcedureParameter.inputParam("prs_order_id", String.class, orderId),
                ProcedureParameter.outputParam("out_result", String.class))
        );
        String result = (String) outputs.get("out_result");
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("Delete order failed!");
    }

    public List<CustomerOrderListOut> getOrderByCustomerByStatus(String customerId, OrderStatusEnum statusEnum, SortPageIn sortPageIn) {
        if (statusEnum.equals(OrderStatusEnum.ALL)) {
            statusEnum = null;
        }

        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("customer_order_list",
                List.of(
                        ProcedureParameter.inputParam("prs_customer_id", String.class, customerId),
                        ProcedureParameter.inputParam("prs_status", String.class, statusEnum),
                        ProcedureParameter.inputParam("prs_sort", String.class, sortPageIn.getSort()),
                        ProcedureParameter.inputParam("prs_properties_sort", String.class, sortPageIn.getPropertiesSort()),
                        ProcedureParameter.inputParam("prn_page_index", Integer.class, sortPageIn.getPage()),
                        ProcedureParameter.inputParam("prn_page_size", Integer.class, sortPageIn.getMaxSize()),
                        ProcedureParameter.inputParam("prs_key_search", String.class, sortPageIn.getKeySearch()),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), CustomerOrderListOut.class
        );

        return (List<CustomerOrderListOut>) outputs.get("out_cur");
    }

    public List<OrderDetailOut> getOrderDetailList(String orderCode) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("order_detail_list",
                List.of(
                        ProcedureParameter.inputParam("prs_order_code", String.class, orderCode),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), OrderDetailOut.class
        );

        System.out.println(outputs.get("out_cur"));

        return (List<OrderDetailOut>) outputs.get("out_cur");
    }
}
