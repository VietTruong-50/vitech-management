package vn.vnpt.api.service;

import vn.vnpt.api.dto.in.CreateUserIn;
import vn.vnpt.api.dto.in.DeleteUserGroupIn;
import vn.vnpt.api.dto.in.UpdateUserIn;
import vn.vnpt.api.dto.in.UserListIn;
import vn.vnpt.api.dto.out.user.GroupListOut;
import vn.vnpt.api.dto.out.user.UserListOut;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.List;

public interface UserService {
    PagingOut<UserListOut> getAllUsers(SortPageIn sortPageIn, UserListIn userListIn);

    void createNewUser(CreateUserIn createUserIn);

    void deleteUser(String userId);

    void deleteUserFromGroup(DeleteUserGroupIn dtoIn);

    List<GroupListOut> groupList();

    void updateUser(UpdateUserIn updateUserIn);
}
