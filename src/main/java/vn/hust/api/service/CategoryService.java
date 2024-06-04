package vn.hust.api.service;

import vn.hust.api.dto.in.CreateCategoryIn;
import vn.hust.api.dto.in.UpdateCategoryIn;
import vn.hust.api.dto.out.category.CategoryDetailOut;
import vn.hust.api.dto.out.category.CategoryListOut;
import vn.hust.common.model.PagingOut;

public interface CategoryService {
    void createNewCategory(CreateCategoryIn createCategoryIn);

    void updateCategory(UpdateCategoryIn updateCategoryIn);

    void deleteCategory(String categoryId);
    PagingOut<CategoryListOut> listAllCategory();
    CategoryDetailOut getCategoryDetail(String productId);
}
