package vn.hust.api.service.impl;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import vn.hust.api.dto.in.CreateUserIn;
import vn.hust.api.dto.in.UpdateUserIn;
import vn.hust.api.service.KeycloakService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

    private final RealmResource realmResource;


    @Override
    public void removeUserInGroup(String groupId, String userId) {
        realmResource.users().get(groupId).leaveGroup(groupId);
    }

    @Override
    public void removeUser(String userId) {
            UsersResource usersResource = realmResource.users();
            usersResource.delete(userId);
    }

    @Override
    public void updateUser(UpdateUserIn updateUserIn) {
        UsersResource usersResource = realmResource.users();

        UserRepresentation userRepresentation = usersResource.get(updateUserIn.getUserId()).toRepresentation();

        userRepresentation.setUsername(updateUserIn.getUserName());
        userRepresentation.setFirstName(updateUserIn.getFirstName());
        userRepresentation.setLastName(updateUserIn.getLastName());
        userRepresentation.setEmail(updateUserIn.getEmail());

        if (updateUserIn.getGroupsToAdd() != null && !updateUserIn.getGroupsToAdd().isEmpty()) {
            for (String groupId : updateUserIn.getGroupsToAdd()) {
                usersResource.get(updateUserIn.getUserId()).joinGroup(groupId);
            }
        }

        if (updateUserIn.getGroupsToRemove() != null && !updateUserIn.getGroupsToRemove().isEmpty()) {
            for (String groupId : updateUserIn.getGroupsToRemove()) {
                usersResource.get(updateUserIn.getUserId()).leaveGroup(groupId);
            }
        }

        // Update the user
        usersResource.get(updateUserIn.getUserId()).update(userRepresentation);
    }

    @Override
    public void addUserToGroup(String groupId, String userId) {
        realmResource.users().get(userId).joinGroup(groupId);
    }

    @Override
    public void addUser(CreateUserIn createUserIn) {
        UsersResource usersResource = realmResource.users();

        // Create a new user representation
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(createUserIn.getUserName());
        userRepresentation.setEmail(createUserIn.getEmail());
        userRepresentation.setFirstName(createUserIn.getFirstName());
        userRepresentation.setLastName(createUserIn.getLastName());
        userRepresentation.setEnabled(true); // Enable the user

        // Set password for the user
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(createUserIn.getPassword());
        credential.setTemporary(false);
        userRepresentation.setCredentials(List.of(credential));

        // Create the user
        Response response = usersResource.create(userRepresentation);

        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            log.info("User created successfully");
        } else {
            log.info("Failed to create user");
        }

    }
}
