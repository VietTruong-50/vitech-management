package vn.vnpt.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.in.CreateSubCategoryIn;
import vn.vnpt.api.dto.in.UpdateSubCategoryIn;
import vn.vnpt.api.dto.out.subCategory.SubCategoryDetailOut;
import vn.vnpt.api.dto.out.subCategory.SubCategoryListOut;
import vn.vnpt.api.repository.SubCategoryRepository;
import vn.vnpt.api.service.SubCategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;

    @Override
    public void createSubCategory(CreateSubCategoryIn createSubCategoryIn) {
        var jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = jwt.getClaims().get("sub").toString();
        subCategoryRepository.createNewSubCategory(createSubCategoryIn, userId);
    }

    @Override
    public void updateSubCategory(UpdateSubCategoryIn updateSubCategoryIn) {
        subCategoryRepository.updateSubCategory(updateSubCategoryIn);
    }

    @Override
    public void deleteSubCategory(String subCategoryId) {
        subCategoryRepository.deleteSubCategory(subCategoryId);
    }


    @Override
    public List<SubCategoryListOut> listAllSubCategories() {
        return subCategoryRepository.getAllSubCategories();
    }

    @Override
    public SubCategoryDetailOut getSubCategoryDetail(String subCategoryId) {
        return subCategoryRepository.getSubCategoryDetail(subCategoryId);
    }

    @Override
    public List<SubCategoryListOut> listSubCategoryByCategory(String categoryId) {
        return subCategoryRepository.listAllByCategory(categoryId);
    }
}
