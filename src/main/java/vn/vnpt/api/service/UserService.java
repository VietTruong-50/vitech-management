package vn.vnpt.api.service;

import vn.vnpt.api.dto.out.user.UserListOut;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

public interface UserService {
    PagingOut<UserListOut> getAllUsers(SortPageIn sortPageIn, String role);
}
