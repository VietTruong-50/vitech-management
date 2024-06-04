package vn.hust.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import vn.hust.api.dto.in.CreateCategoryIn;
import vn.hust.api.dto.in.UpdateCategoryIn;
import vn.hust.api.service.CategoryService;
import vn.hust.common.AbstractResponseController;

import java.util.Collections;

@RestController
@RequestMapping("/v1/management/category")
@Slf4j
@RequiredArgsConstructor
public class CategoryController extends AbstractResponseController {

    private final CategoryService categoryService;

    @PostMapping(value = "/create-new")
    @PreAuthorize("hasAnyAuthority('staff', 'admin') ")
    public DeferredResult<ResponseEntity<?>> createNewCategory(@Valid @RequestBody CreateCategoryIn createCategoryIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /category/create-new");
            categoryService.createNewCategory(createCategoryIn);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }


    @PostMapping(value = "/update")
    @PreAuthorize("hasAnyAuthority('staff', 'admin') ")
    public DeferredResult<ResponseEntity<?>> updateCategory(@Valid @RequestBody UpdateCategoryIn updateCategoryIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /category/update");
            categoryService.updateCategory(updateCategoryIn);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }


    @PostMapping(value = "/delete", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin') ")
    public DeferredResult<ResponseEntity<?>> deleteCategory(@RequestParam String categoryId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /category/delete/{}", categoryId);
            categoryService.deleteCategory(categoryId);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @GetMapping(value = "/list-filter", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin') ")
    public DeferredResult<ResponseEntity<?>> getAllCategory() {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /category/list-filter");
            var result = categoryService.listAllCategory();
            log.info("[RESPONSE]: res: Success!");
            return result;
        });
    }

    @GetMapping(value = "/detail", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin') ")
    public DeferredResult<ResponseEntity<?>> getCategoryById(@RequestParam String categoryId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /category/detail/{}", categoryId);
            var rs = categoryService.getCategoryDetail(categoryId);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

}
