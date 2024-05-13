package vn.vnpt.api.dto.out.user;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

import java.util.List;
import java.util.Map;

@Data
public class UserListOut {
    @Col("id")
    private String id;
    @Col("email")
    private String email;
    @Col("first_name")
    private String firstName;
    @Col("last_name")
    private String lastName;
    @Col("username")
    private String userName;

    private List<Group> groupList;


    @Data
    public static class Group{
        @Col("group_name")
        private String groupName;
        @Col("group_id")
        private String groupId;
    }

}
