package vn.vnpt.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.dto.in.*;
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

    @PostMapping(value = "/list-filter", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin') ")
    public DeferredResult<ResponseEntity<?>> getAllUsers(@RequestBody @Valid UserListIn userListIn, SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /user/list-filter");
            var rs = userService.getAllUsers(sortPageIn, userListIn);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @PostMapping(value = "/create-new", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin') ")
    public DeferredResult<ResponseEntity<?>> createNewUser(@RequestBody @Valid CreateUserIn createUserIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /user/create-new + {}", createUserIn);
            userService.createNewUser(createUserIn);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @PostMapping(value = "/update", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin') ")
    public DeferredResult<ResponseEntity<?>> updateUser(@RequestBody @Valid UpdateUserIn userRequest) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /user/update + {}", userRequest);
            userService.updateUser(userRequest);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @PostMapping(value = "/delete", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin') ")
    public DeferredResult<ResponseEntity<?>> deleteUser(@RequestBody @Valid DeleteUserIn dto) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /delete/{}", dto.getUserId());
            userService.deleteUser(dto.getUserId());
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @PostMapping(value = "/delete/group", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin') ")
    public DeferredResult<ResponseEntity<?>> deleteUserFromGroup(@RequestBody @Valid DeleteUserGroupIn dto) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /delete/group + userId: {} + groupId: {}", dto.getUserId(), dto.getGroupId());
            userService.deleteUserFromGroup(dto);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @GetMapping(value = "/group/list-filter", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin')")
    public DeferredResult<ResponseEntity<?>> groupList() {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /group/list-filter");
            var rs = userService.groupList();
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/customers/detail", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> getCustomerDetail(@RequestParam String customerId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /customers/detail, {}", customerId);
            var rs = customerService.getCustomerDetail(customerId);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/customers/list-filter", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
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
