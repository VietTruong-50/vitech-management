package vn.vnpt.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import vn.vnpt.api.dto.in.CreateProductIn;
import vn.vnpt.api.dto.in.DeleteProductIn;
import vn.vnpt.api.dto.in.ProductFilterIn;
import vn.vnpt.api.dto.in.UpdateProductIn;
import vn.vnpt.api.service.InventoryService;
import vn.vnpt.common.AbstractResponseController;
import vn.vnpt.common.model.SortPageIn;

import java.util.Collections;

@RestController
@RequestMapping(value = "/v1/management/product", produces = "application/json")
@Slf4j
@RequiredArgsConstructor
public class InventoryController extends AbstractResponseController {

    private final InventoryService inventoryService;


    @PostMapping(value = "/create-new", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('staff', 'admin') ")
    public DeferredResult<ResponseEntity<?>> createNewProduct(@RequestPart(value = "images") MultipartFile[] images,
                                                              @RequestPart(value = "featureImage") MultipartFile featureImage,
                                                              @RequestPart CreateProductIn productIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/product/create-new");
            inventoryService.createNewProduct(productIn, featureImage, images);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @PostMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> updateProduct(@RequestPart(value = "images", required = false) MultipartFile[] images,
                                                           @RequestPart(value = "featureImage", required = false) MultipartFile featureImage,
                                                           @RequestPart UpdateProductIn updatedProduct) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /products/update");
            inventoryService.updateProduct(updatedProduct, featureImage, images);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @GetMapping(value = {"/list-filter"}, produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin') ")
    public DeferredResult<ResponseEntity<?>> getAllProduct(SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /product/list-filter");
            var rs = inventoryService.listAllProducts(sortPageIn);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = {"/detail"}, produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> findProductById(@RequestParam String productId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /products/detail");
            var rs = inventoryService.getProductDetail(productId);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @PostMapping(value = "/filter", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> filterProducts(@RequestBody ProductFilterIn productFilterIn, SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /products/filter");
            var rs = inventoryService.filterProducts(productFilterIn, sortPageIn);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @PostMapping(value = "/delete", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> deleteProduct(@RequestBody DeleteProductIn deleteProductIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /products/delete");
            inventoryService.deleteProduct(deleteProductIn.getProductId());
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @GetMapping(value = {"/attribute/list"}, produces = "application/json")
    @PreAuthorize("hasAnyAuthority('staff', 'admin')")
    public DeferredResult<ResponseEntity<?>> getAllAttribute(String categoryId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /products/attribute/list");
            var rs = inventoryService.getAllAttribute(categoryId);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

}
