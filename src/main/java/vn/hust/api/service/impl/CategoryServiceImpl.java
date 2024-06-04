package vn.hust.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import vn.hust.api.dto.in.CreateCategoryIn;
import vn.hust.api.dto.in.UpdateCategoryIn;
import vn.hust.api.dto.out.category.CategoryDetailOut;
import vn.hust.api.dto.out.category.CategoryListOut;
import vn.hust.api.repository.CategoryRepository;
import vn.hust.api.service.CategoryService;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void createNewCategory(CreateCategoryIn createCategoryIn) {
        var jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = jwt.getClaims().get("sub").toString();

        categoryRepository.createNewCategory(createCategoryIn, userId);
    }

    @Override
    public void updateCategory(UpdateCategoryIn updateCategoryIn) {
        categoryRepository.updateCategory(updateCategoryIn);
    }

    @Override
    public void deleteCategory(String categoryId) {
        categoryRepository.deleteCategory(categoryId);
    }


    @Override
    public PagingOut<CategoryListOut> listAllCategory() {
        return categoryRepository.listAllCategories(new SortPageIn());
    }

    @Override
    public CategoryDetailOut getCategoryDetail(String categoryId) {
        return categoryRepository.getCategoryDetail(categoryId);
    }
}
