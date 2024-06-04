package vn.hust.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.hust.api.dto.out.user.GroupListOut;
import vn.hust.api.dto.out.user.UserListOut;
import vn.hust.api.repository.helper.ProcedureCallerV3;
import vn.hust.api.repository.helper.ProcedureParameter;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final ProcedureCallerV3 procedureCallerV3;

    public void updateUser() {

    }

    public UserListOut getUserDetail(String userId) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("user_detail",
                List.of(
                        ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), UserListOut.class
        );

        List<UserListOut> outList = (List<UserListOut>) outputs.get("out_cur");

        if(outList.isEmpty()){
            throw new RuntimeException("user_detail not found");
        }

        return outList.get(0);
    }

    public PagingOut<UserListOut> getAllUser(SortPageIn sortPageIn, String role) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("user_list_filter",
                List.of(
                        ProcedureParameter.inputParam("prs_properties_sort", String.class, sortPageIn.getPropertiesSort()),
                        ProcedureParameter.inputParam("prs_sort", String.class, sortPageIn.getSort()),
                        ProcedureParameter.inputParam("prn_page_index", Integer.class, sortPageIn.getPage()),
                        ProcedureParameter.inputParam("prn_page_size", Integer.class, sortPageIn.getMaxSize()),
                        ProcedureParameter.inputParam("prs_key_search", String.class, sortPageIn.getKeySearch()),
                        ProcedureParameter.inputParam("prs_role", String.class, role),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), UserListOut.class
        );

        List<UserListOut> outList = (List<UserListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

    public List<UserListOut.Group> getUserGroup(String userId) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("user_group",
                List.of(
                        ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                        ProcedureParameter.refCursorParam("out_cur")
                ), UserListOut.Group.class
        );

        System.out.println(outputs);

        return (List<UserListOut.Group>) outputs.get("out_cur");
    }

    public List<GroupListOut> getListGroup() {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("group_user_list",
                List.of(
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), GroupListOut.class
        );

        return (List<GroupListOut>) outputs.get("out_cur");
    }
}
