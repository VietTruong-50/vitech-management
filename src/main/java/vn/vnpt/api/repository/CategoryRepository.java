package vn.vnpt.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.vnpt.api.dto.in.CreateCategoryIn;
import vn.vnpt.api.dto.in.UpdateCategoryIn;
import vn.vnpt.api.dto.out.category.CategoryDetailOut;
import vn.vnpt.api.dto.out.category.CategoryListOut;
import vn.vnpt.api.repository.helper.ProcedureCallerV3;
import vn.vnpt.api.repository.helper.ProcedureParameter;
import vn.vnpt.common.constant.DatabaseStatus;
import vn.vnpt.common.exception.ApiErrorException;
import vn.vnpt.common.exception.NotFoundException;
import vn.vnpt.common.exception.model.ApiError;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class CategoryRepository {
    private final ProcedureCallerV3 procedureCallerV3;

    public void createNewCategory(CreateCategoryIn createCategoryIn, String userId) {
        var outputs = procedureCallerV3.callNoRefCursor("category_create_new", List.of(
                ProcedureParameter.inputParam("prs_category_name", String.class, createCategoryIn.getCategoryName()),
                ProcedureParameter.inputParam("prs_description", String.class, createCategoryIn.getDescription()),
                ProcedureParameter.inputParam("prs_icon", String.class, createCategoryIn.getIcon()),
                ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                ProcedureParameter.outputParam("out_result", String.class))
        );
        String result = (String) outputs.get("out_result");
        if (DatabaseStatus.isExist(result)) throw new ApiErrorException(ApiError.fieldExist(result));
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("Create category failed!");
    }

    public void updateCategory(UpdateCategoryIn updateCategoryIn) {
        var outputs = procedureCallerV3.callNoRefCursor("category_update", List.of(
                ProcedureParameter.inputParam("prs_category_id", String.class, updateCategoryIn.getCategoryId()),
                ProcedureParameter.inputParam("prs_category_name", String.class, updateCategoryIn.getCategoryName()),
                ProcedureParameter.inputParam("prs_description", String.class, updateCategoryIn.getDescription()),
                ProcedureParameter.inputParam("prs_image_url", String.class, updateCategoryIn.getImageUrl()),
                ProcedureParameter.outputParam("out_result", String.class))
        );
        String result = (String) outputs.get("out_result");
        if (DatabaseStatus.isExist(result)) throw new ApiErrorException(ApiError.fieldExist(result));
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("Update category failed!");
    }

    public void deleteCategory(String categoryId) {
        var outputs = procedureCallerV3.callNoRefCursor("category_delete",
                List.of(ProcedureParameter.inputParam("prs_category_id", String.class, categoryId),
                        ProcedureParameter.outputParam("out_result", String.class))
        );
        String result = (String) outputs.get("out_result");
        if (DatabaseStatus.isExist(result)) throw new ApiErrorException(ApiError.fieldExist(result));
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("Delete category failed!");
    }

    public CategoryDetailOut getCategoryDetail(String categoryId) {
        var outputs = procedureCallerV3.callOneRefCursor("category_detail",
                List.of(
                        ProcedureParameter.inputParam("prs_category_id", String.class, categoryId),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ),
                CategoryDetailOut.class
        );
        List<CategoryDetailOut> categoryDetailOuts = (List<CategoryDetailOut>) outputs.get("out_cur");
        if (categoryDetailOuts == null || categoryDetailOuts.isEmpty()) {
            throw new NotFoundException("Category not found!");
        }

        return categoryDetailOuts.get(0);
    }

    public PagingOut<CategoryListOut> listAllCategories(SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("category_list_filter",
                List.of(
                        ProcedureParameter.inputParam("prs_key_search", String.class, sortPageIn.getKeySearch()),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), CategoryListOut.class
        );

        List<CategoryListOut> categoryListOuts =  (List<CategoryListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, categoryListOuts);
    }
}
