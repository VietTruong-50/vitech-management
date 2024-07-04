package vn.hust.api.service;

import org.springframework.web.multipart.MultipartFile;
import vn.hust.api.dto.in.CreateProductIn;
import vn.hust.api.dto.in.ProductFilterIn;
import vn.hust.api.dto.in.UpdateProductIn;
import vn.hust.api.dto.out.product.ProductDetailOut;
import vn.hust.api.dto.out.product.ProductListOut;
import vn.hust.api.dto.out.product.attribute.AttributeListOut;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.util.List;

public interface InventoryService {
    void createNewProduct(CreateProductIn createProductIn, MultipartFile file, MultipartFile[] images);
    void updateProduct(UpdateProductIn updateProductIn, MultipartFile file, MultipartFile[] images, String featureImageChange);
    void deleteProduct(String productId);
    PagingOut<ProductListOut> listAllProducts(SortPageIn sortPageIn);
    ProductDetailOut getProductDetail(String productId);
    PagingOut<ProductListOut> filterProducts(ProductFilterIn productFilterIn, SortPageIn sortPageIn);
    PagingOut<ProductListOut> findAllBySubCategory(String subCategoryId, SortPageIn sortPageIn);
    List<AttributeListOut> getAllAttribute(String categoryId);
}
