package vn.vnpt.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.in.CreateCategoryIn;
import vn.vnpt.api.dto.in.UpdateCategoryIn;
import vn.vnpt.api.dto.out.category.CategoryDetailOut;
import vn.vnpt.api.dto.out.category.CategoryListOut;
import vn.vnpt.api.repository.CategoryRepository;
import vn.vnpt.api.service.CategoryService;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

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
