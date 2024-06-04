package vn.hust.api.service;

import vn.hust.api.dto.in.CreateSubCategoryIn;
import vn.hust.api.dto.in.UpdateSubCategoryIn;
import vn.hust.api.dto.out.subCategory.SubCategoryDetailOut;
import vn.hust.api.dto.out.subCategory.SubCategoryListOut;

import java.util.List;

public interface SubCategoryService {
    void createSubCategory(CreateSubCategoryIn createSubCategoryIn);
    void updateSubCategory(UpdateSubCategoryIn updateSubCategoryIn);
    void deleteSubCategory(String subCategoryId);
    List<SubCategoryListOut> listAllSubCategories();
    SubCategoryDetailOut getSubCategoryDetail(String subCategoryId);
    List<SubCategoryListOut> listSubCategoryByCategory(String categoryId);
}
