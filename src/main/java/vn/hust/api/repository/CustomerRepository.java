package vn.hust.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.hust.api.dto.out.customer.CustomerDetailOut;
import vn.hust.api.dto.out.customer.CustomerListOut;
import vn.hust.api.repository.helper.ProcedureCallerV3;
import vn.hust.api.repository.helper.ProcedureParameter;
import vn.hust.common.exception.NotFoundException;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {
    private final ProcedureCallerV3 procedureCallerV3;

    public PagingOut<CustomerListOut> findAllCustomers(SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("customer_list_filter",
                List.of(
                        ProcedureParameter.inputParam("prs_properties_sort", String.class, sortPageIn.getPropertiesSort()),
                        ProcedureParameter.inputParam("prs_sort", String.class, sortPageIn.getSort()),
                        ProcedureParameter.inputParam("prn_page_index", Integer.class, sortPageIn.getPage()),
                        ProcedureParameter.inputParam("prn_page_size", Integer.class, sortPageIn.getMaxSize()),
                        ProcedureParameter.inputParam("prs_key_search", String.class, sortPageIn.getKeySearch()),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), CustomerListOut.class
        );

        List<CustomerListOut> outList = (List<CustomerListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

    public CustomerDetailOut getCustomerDetail(String customerId) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("customer_detail",
                List.of(
                        ProcedureParameter.inputParam("prs_customer_id", String.class, customerId),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.outputParam("out_total_order", BigDecimal.class),
                        ProcedureParameter.outputParam("out_total_success_order", BigDecimal.class),
                        ProcedureParameter.outputParam("out_total_failed_order", BigDecimal.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), CustomerDetailOut.class
        );

        List<CustomerDetailOut> outList = (List<CustomerDetailOut>) outputs.get("out_cur");

        if (outList.isEmpty()) {
            throw new NotFoundException("Không tìm thấy khách hàng");
        }

        var rs = outList.get(0);
        rs.setTotalOrder((BigDecimal) outputs.get("out_total_order"));
        rs.setTotalSuccessOrder((BigDecimal) outputs.get("out_total_success_order"));
        rs.setTotalFailedOrder((BigDecimal) outputs.get("out_total_failed_order"));

        return rs;
    }


}
