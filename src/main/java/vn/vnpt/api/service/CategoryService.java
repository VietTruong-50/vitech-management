package vn.vnpt.api.service;

import vn.vnpt.api.dto.in.CreateCategoryIn;
import vn.vnpt.api.dto.in.UpdateCategoryIn;
import vn.vnpt.api.dto.out.category.CategoryDetailOut;
import vn.vnpt.api.dto.out.category.CategoryListOut;
import vn.vnpt.common.model.PagingOut;

public interface CategoryService {
    void createNewCategory(CreateCategoryIn createCategoryIn);

    void updateCategory(UpdateCategoryIn updateCategoryIn);

    void deleteCategory(String categoryId);
    PagingOut<CategoryListOut> listAllCategory();
    CategoryDetailOut getCategoryDetail(String productId);
}
