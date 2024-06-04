package vn.hust.api.service;

import vn.hust.api.dto.in.CreateUserIn;
import vn.hust.api.dto.in.DeleteUserGroupIn;
import vn.hust.api.dto.in.UpdateUserIn;
import vn.hust.api.dto.in.UserListIn;
import vn.hust.api.dto.out.user.GroupListOut;
import vn.hust.api.dto.out.user.UserListOut;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.util.List;

public interface UserService {
    PagingOut<UserListOut> getAllUsers(SortPageIn sortPageIn, UserListIn userListIn);

    void createNewUser(CreateUserIn createUserIn);

    void deleteUser(String userId);

    void deleteUserFromGroup(DeleteUserGroupIn dtoIn);

    List<GroupListOut> groupList();

    void updateUser(UpdateUserIn updateUserIn);
}
