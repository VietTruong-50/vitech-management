package vn.vnpt.api.service;

import vn.vnpt.api.dto.in.CreateUserIn;
import vn.vnpt.api.dto.in.UpdateUserIn;

public interface KeycloakService {
    void removeUserInGroup(String groupId, String userId);

    void removeUser(String userId);

    void updateUser(UpdateUserIn updateUserIn);

    void addUserToGroup(String groupId, String userId);

    void addUser(CreateUserIn createUserIn);
}
