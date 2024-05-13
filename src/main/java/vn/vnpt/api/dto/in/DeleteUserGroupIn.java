package vn.vnpt.api.dto.in;

import lombok.Data;

@Data
public class DeleteUserGroupIn {
    private String userId;
    private String groupId;
}
