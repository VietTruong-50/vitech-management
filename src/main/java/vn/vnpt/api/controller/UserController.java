package vn.vnpt.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.dto.in.UpdateUserIn;
import vn.vnpt.api.enums.OrderStatusEnum;
import vn.vnpt.api.service.CustomerService;
import vn.vnpt.api.service.UserService;
import vn.vnpt.common.AbstractResponseController;
import vn.vnpt.common.model.SortPageIn;

import java.util.Collections;

@RestController
@RequestMapping("/v1/management/user")
@RequiredArgsConstructor
@Slf4j
public class UserController extends AbstractResponseController {
    private final CustomerService customerService;
    private final UserService userService;

    @GetMapping(value = "/list-filter", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin') ")
    public DeferredResult<ResponseEntity<?>> getAllUsers(SortPageIn sortPageIn, @RequestParam("role") String role) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /user/list-filter");
            var rs = userService.getAllUsers(sortPageIn, role);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @PostMapping(value = "/user/{userId}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin') ")
    public DeferredResult<ResponseEntity<?>> updateUser(@PathVariable("userId") String userId, @RequestBody UpdateUserIn userRequest) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /user/{}", userId);


            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @PostMapping(value = "/delete/{userId}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin') ")
    public DeferredResult<ResponseEntity<?>> deleteUser(@PathVariable("userId") String userId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /detail/{}", userId);

            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @GetMapping(value = "/customers/detail", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin') ")
    public DeferredResult<ResponseEntity<?>> getCustomerDetail(@RequestParam String customerId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /customers/detail, {}", customerId);
            var rs = customerService.getCustomerDetail(customerId);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/customers/list-filter", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin') ")
    public DeferredResult<ResponseEntity<?>> findAllCustomers(SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /customers/list-filter");
            var rs = customerService.findAllCustomers(sortPageIn);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }


    @GetMapping(value = "/customers/order", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> getCustomerOrderList(@RequestParam String customerId, @RequestParam OrderStatusEnum statusEnum, SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /management/user/customers/order");
            var rs = customerService.getCustomerOrderList(customerId, statusEnum, sortPageIn);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }
}
