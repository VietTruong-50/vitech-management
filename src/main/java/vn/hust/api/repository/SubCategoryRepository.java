package vn.hust.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.hust.api.dto.in.CreateSubCategoryIn;
import vn.hust.api.dto.in.UpdateSubCategoryIn;
import vn.hust.api.dto.out.subCategory.SubCategoryDetailOut;
import vn.hust.api.dto.out.subCategory.SubCategoryListOut;
import vn.hust.api.repository.helper.ProcedureCallerV3;
import vn.hust.api.repository.helper.ProcedureParameter;
import vn.hust.common.constant.DatabaseStatus;
import vn.hust.common.exception.ApiErrorException;
import vn.hust.common.exception.NotFoundException;
import vn.hust.common.exception.model.ApiError;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class SubCategoryRepository {
    private final ProcedureCallerV3 procedureCallerV3;

    public void createNewSubCategory(CreateSubCategoryIn createSubCategoryIn, String userId) {
        var outputs = procedureCallerV3.callNoRefCursor("category_create_new", List.of(
                ProcedureParameter.inputParam("prs_name", String.class, createSubCategoryIn.getSubCategoryName()),
                ProcedureParameter.inputParam("prs_description", String.class, createSubCategoryIn.getDescription()),
                ProcedureParameter.inputParam("prs_icon", String.class, createSubCategoryIn.getIcon()),
                ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                ProcedureParameter.outputParam("out_result", String.class))
        );
        String result = (String) outputs.get("out_result");
        if (DatabaseStatus.isExist(result)) throw new ApiErrorException(ApiError.fieldExist(result));
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("Create subcategory failed!");
    }

    public void updateSubCategory(UpdateSubCategoryIn updateSubCategoryIn) {
        var outputs = procedureCallerV3.callNoRefCursor("subcategory_update", List.of(
                ProcedureParameter.inputParam("prs_name", String.class, updateSubCategoryIn.getSubCategoryName()),
                ProcedureParameter.inputParam("prs_description", String.class, updateSubCategoryIn.getDescription()),
                ProcedureParameter.inputParam("prs_icon", String.class, updateSubCategoryIn.getIcon()),
                ProcedureParameter.outputParam("out_result", String.class))
        );
        String result = (String) outputs.get("out_result");
        if (DatabaseStatus.isExist(result)) throw new ApiErrorException(ApiError.fieldExist(result));
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("Update subcategory failed!");
    }

    public void deleteSubCategory(String subCategoryId) {
        var outputs = procedureCallerV3.callNoRefCursor("subcategory_delete", List.of(
                ProcedureParameter.inputParam("prs_sub_category_id", String.class, subCategoryId),
                ProcedureParameter.outputParam("out_result", String.class))
        );
        String result = (String) outputs.get("out_result");
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("Delete subcategory failed!");
    }

    public SubCategoryDetailOut getSubCategoryDetail(String subCategoryId) {
        var outputs = procedureCallerV3.callOneRefCursor("subcategory_detail",
                List.of(
                        ProcedureParameter.inputParam("prs_sub_category_id", String.class, subCategoryId),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ),
                SubCategoryDetailOut.class
        );
        List<SubCategoryDetailOut> outList = (List<SubCategoryDetailOut>) outputs.get("out_cur");
        if (outList == null || outList.isEmpty()) {
            throw new NotFoundException("Subcategory not found!");
        }

        return outList.get(0);
    }

    public List<SubCategoryListOut> getAllSubCategories() {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("subcategory_list",
                List.of(
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), SubCategoryListOut.class
        );

        List<SubCategoryListOut> outList = (List<SubCategoryListOut>) outputs.get("out_cur");

        return outList;
    }
    public List<SubCategoryListOut> listAllByCategory(String categoryId) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("subcategory_list_by_category",
                List.of(
                        ProcedureParameter.inputParam("prs_category_id", String.class, categoryId),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), SubCategoryListOut.class
        );

        List<SubCategoryListOut> outList = (List<SubCategoryListOut>) outputs.get("out_cur");

        return outList;
    }
}
