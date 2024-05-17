package vn.vnpt.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.dto.in.OrderDetailIn;
import vn.vnpt.api.dto.in.UpdateOrderStatus;
import vn.vnpt.api.enums.OrderStatusEnum;
import vn.vnpt.api.service.OrderService;
import vn.vnpt.common.AbstractResponseController;
import vn.vnpt.common.model.SortPageIn;

import java.util.Collections;

@RestController
@RequestMapping(value = "/v1/management/order", produces = "application/json")
@Slf4j
@RequiredArgsConstructor
public class OrderController extends AbstractResponseController {
    private final OrderService orderService;

    @GetMapping(value = "/list-filter")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> listOrder(OrderStatusEnum orderStatusEnum, SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /management/order/list-filter");
            var rs = orderService.getAllOrders(orderStatusEnum, sortPageIn);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/detail")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> getOrderDetail(@RequestBody @Valid OrderDetailIn orderDetailIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /management/order/detail");
            var rs = orderService.getOrderDetail(orderDetailIn.getOrderId());
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }


    @PostMapping(value = "/update-status")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> updateOrderStatus(@RequestBody @Valid UpdateOrderStatus updateOrderStatus) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /management/order/update-status");
            orderService.updateOrderStatus(updateOrderStatus);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

}
