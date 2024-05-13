package vn.vnpt.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.in.CreateUserIn;
import vn.vnpt.api.dto.in.DeleteUserGroupIn;
import vn.vnpt.api.dto.in.UpdateUserIn;
import vn.vnpt.api.dto.in.UserListIn;
import vn.vnpt.api.dto.out.user.GroupListOut;
import vn.vnpt.api.dto.out.user.UserListOut;
import vn.vnpt.api.repository.UserRepository;
import vn.vnpt.api.service.KeycloakService;
import vn.vnpt.api.service.UserService;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    @Override
    public PagingOut<UserListOut> getAllUsers(SortPageIn sortPageIn, UserListIn userListIn) {
        PagingOut<UserListOut> userList = userRepository.getAllUser(sortPageIn, userListIn.getRoleId());
        Set<String> uniqueUserIds = new HashSet<>(); // Set to store unique user IDs
        List<UserListOut> uniqueUserList = new ArrayList<>(); // List to store unique users

        for (var it : userList.getData()) {
            if (uniqueUserIds.add(it.getId())) { // Add user ID to set, returns true if it's a new ID
                List<UserListOut.Group> groupList = userRepository.getUserGroup(it.getId());
                it.setGroupList(groupList);
                uniqueUserList.add(it); // Add user to the list of unique users
            }
        }

        // Create a new PagingOut object with the unique user list
        return PagingOut.of(userList.getMaxSize(), sortPageIn, uniqueUserList);

    }

    @Override
    public void createNewUser(CreateUserIn createUserIn){
        keycloakService.addUser(createUserIn);
    }

    @Override
    public void deleteUser(String userId) {
        keycloakService.removeUser(userId);
    }

    @Override
    public void deleteUserFromGroup(DeleteUserGroupIn dtoIn) {
        keycloakService.removeUserInGroup(dtoIn.getGroupId(), dtoIn.getUserId());
    }

    @Override
    public List<GroupListOut> groupList() {
        return userRepository.getListGroup();
    }

    @Override
    public void updateUser(UpdateUserIn updateUserIn) {
        keycloakService.updateUser(updateUserIn);
    }
}
