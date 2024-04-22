package vn.vnpt.api.service;

import org.springframework.web.multipart.MultipartFile;
import vn.vnpt.api.dto.in.CreateProductIn;
import vn.vnpt.api.dto.in.ProductFilterIn;
import vn.vnpt.api.dto.in.UpdateProductIn;
import vn.vnpt.api.dto.out.product.ProductDetailOut;
import vn.vnpt.api.dto.out.product.ProductListOut;
import vn.vnpt.api.dto.out.product.attribute.AttributeListOut;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.List;

public interface InventoryService {
    void createNewProduct(CreateProductIn createProductIn, MultipartFile file, MultipartFile[] images);
    void updateProduct(UpdateProductIn updateProductIn, MultipartFile file, MultipartFile[] images);
    void deleteProduct(String productId);
    PagingOut<ProductListOut> listAllProducts(SortPageIn sortPageIn);
    ProductDetailOut getProductDetail(String productId);
    PagingOut<ProductListOut> filterProducts(ProductFilterIn productFilterIn, SortPageIn sortPageIn);
    PagingOut<ProductListOut> findAllBySubCategory(String subCategoryId, SortPageIn sortPageIn);
    List<AttributeListOut> getAllAttribute();
}
