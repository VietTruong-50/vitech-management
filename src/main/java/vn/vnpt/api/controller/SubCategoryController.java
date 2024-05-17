package vn.vnpt.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.dto.in.CreateSubCategoryIn;
import vn.vnpt.api.dto.in.UpdateSubCategoryIn;
import vn.vnpt.api.service.SubCategoryService;
import vn.vnpt.common.AbstractResponseController;
import vn.vnpt.common.model.SortPageIn;

import java.util.Collections;

@RestController
@RequestMapping("/v1/management/sub-category")
@Slf4j
@RequiredArgsConstructor
public class SubCategoryController  extends AbstractResponseController {

    private final SubCategoryService subCategoryService;

    @PostMapping(value = "/create-new")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> createNewSubCategory(@RequestBody @Valid CreateSubCategoryIn subCategoryIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /create-new");
            subCategoryService.createSubCategory(subCategoryIn);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @GetMapping(value = "/list-filter", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> getAllSubCategory() {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /subCategories");
            var rs = subCategoryService.listAllSubCategories();
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/category/list-filter", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> getSubCategoriesByCategory(@RequestParam String categoryId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /category/list-filter");
            var rs = subCategoryService.listSubCategoryByCategory(categoryId);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/detail", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> getSubCategoryById(@RequestParam String subcategoryId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /detail");
            var rs =subCategoryService.getSubCategoryDetail(subcategoryId);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @PostMapping(value = "/update", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> updateSubCategory(@RequestBody @Valid UpdateSubCategoryIn subCategoryIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /sub-category/update");
            subCategoryService.updateSubCategory(subCategoryIn);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @PostMapping(value = "/delete", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> deleteCategory(@RequestParam String subCategoryId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /sub-category/delete");
            subCategoryService.deleteSubCategory(subCategoryId);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }
}
