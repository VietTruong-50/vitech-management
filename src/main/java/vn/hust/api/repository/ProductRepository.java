package vn.hust.api.repository;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.hust.api.dto.in.CreateProductIn;
import vn.hust.api.dto.in.ProductFilterIn;
import vn.hust.api.dto.in.UpdateProductIn;
import vn.hust.api.dto.out.product.ProductAttributeOut;
import vn.hust.api.dto.out.product.ProductDetailOut;
import vn.hust.api.dto.out.product.ProductListOut;
import vn.hust.api.dto.out.product.attribute.AttributeListOut;
import vn.hust.api.repository.helper.ProcedureCallerV3;
import vn.hust.api.repository.helper.ProcedureParameter;
import vn.hust.common.constant.DatabaseStatus;
import vn.hust.common.exception.ApiErrorException;
import vn.hust.common.exception.NotFoundException;
import vn.hust.common.exception.model.ApiError;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class ProductRepository {
    private final ProcedureCallerV3 procedureCallerV3;
    private final Gson gson;

    public List<AttributeListOut> getAllAttribute() {
        var outputs = procedureCallerV3.callOneRefCursor("attribute_list", List.of(
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")),
                AttributeListOut.class
        );
        String result = (String) outputs.get("out_result");
        if (DatabaseStatus.isExist(result)) throw new ApiErrorException(ApiError.fieldExist(result));
        return (List<AttributeListOut>) outputs.get("out_cur");
    }

    public void addAttribute(String value, String attributeId, String productId) {
        var outputs = procedureCallerV3.callNoRefCursor("attribute_product_create_new", List.of(
                ProcedureParameter.inputParam("prs_attribute_id", String.class, attributeId),
                ProcedureParameter.inputParam("prs_product_id", String.class, productId),
                ProcedureParameter.inputParam("prs_value", String.class, value),
                ProcedureParameter.outputParam("out_result", String.class))
        );
        String result = (String) outputs.get("out_result");
        if (DatabaseStatus.isExist(result)) throw new ApiErrorException(ApiError.fieldExist(result));
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("Create product failed!");
    }

    public void updateAttribute(String value, String attributeId, String productId) {
        var outputs = procedureCallerV3.callNoRefCursor("attribute_product_update", List.of(
                ProcedureParameter.inputParam("prs_attribute_id", String.class, attributeId),
                ProcedureParameter.inputParam("prs_product_id", String.class, productId),
                ProcedureParameter.inputParam("prs_value", String.class, value),
                ProcedureParameter.outputParam("out_result", String.class))
        );
        String result = (String) outputs.get("out_result");
        if (DatabaseStatus.isExist(result)) throw new ApiErrorException(ApiError.fieldExist(result));
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("attribute_product_update failed!");
    }

    public String createNewProduct(CreateProductIn createProductIn, String userId, String url, List<String> imageUrlList) {
        var outputs = procedureCallerV3.callNoRefCursor("product_create_new", List.of(
                        ProcedureParameter.inputParam("prs_name", String.class, createProductIn.getName()),
                        ProcedureParameter.inputParam("prs_product_code", String.class, createProductIn.getProductCode()),
                        ProcedureParameter.inputParam("prs_description", String.class, createProductIn.getDescription()),
                        ProcedureParameter.inputParam("prs_quantity", Integer.class, createProductIn.getQuantity()),
                        ProcedureParameter.inputParam("prs_summary", String.class, createProductIn.getSummary()),
                        ProcedureParameter.inputParam("prs_feature_image_link", String.class, url),
                        ProcedureParameter.inputParam("prs_parameters", String.class, gson.toJson(createProductIn.getParameters())),
                        ProcedureParameter.inputParam("prs_price", Double.class, createProductIn.getPrice()),
                        ProcedureParameter.inputParam("prs_actual_price", Double.class, createProductIn.getActualPrice()),
                        ProcedureParameter.inputParam("prs_tag_ids", String.class, createProductIn.getTagIds() != null ? String.join(",", createProductIn.getTagIds()) : ""),
                        ProcedureParameter.inputParam("prs_subcategory_id", String.class, createProductIn.getSubcategoryId()),
                        ProcedureParameter.inputParam("prs_category_id", String.class, createProductIn.getCategoryId()),
                        ProcedureParameter.inputParam("prs_product_status", Integer.class, createProductIn.getStatus().value),
                        ProcedureParameter.inputParam("prs_image_links", String.class, imageUrlList),
                        ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.outputParam("out_product_id", String.class)
                )
        );
        String result = (String) outputs.get("out_result");
        if (DatabaseStatus.isExist(result)) throw new ApiErrorException(ApiError.fieldExist(result));
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("Create product failed!");
        return (String) outputs.get("out_product_id");
    }

    public void updateProduct(UpdateProductIn updateProductIn, String url, List<String> imageUrlList) {
        var outputs = procedureCallerV3.callNoRefCursor("product_update", List.of(
                        ProcedureParameter.inputParam("prs_product_id", String.class, updateProductIn.getProductId()),
                        ProcedureParameter.inputParam("prs_name", String.class, updateProductIn.getName()),
                        ProcedureParameter.inputParam("prs_product_code", String.class, updateProductIn.getProductCode()),
                        ProcedureParameter.inputParam("prs_description", String.class, updateProductIn.getDescription()),
                        ProcedureParameter.inputParam("prs_quantity", Integer.class, updateProductIn.getQuantity()),
                        ProcedureParameter.inputParam("prs_summary", String.class, updateProductIn.getSummary()),
                        ProcedureParameter.inputParam("prs_feature_image_link", String.class, url),
                        ProcedureParameter.inputParam("prs_parameters", String.class, gson.toJson(updateProductIn.getParameters())),
                        ProcedureParameter.inputParam("prs_price", Double.class, updateProductIn.getPrice()),
                        ProcedureParameter.inputParam("prs_actual_price", Double.class, updateProductIn.getActualPrice()),
                        ProcedureParameter.inputParam("prs_tag_ids", String.class, updateProductIn.getTagIds() != null ? String.join(",", updateProductIn.getTagIds()) : ""),
                        ProcedureParameter.inputParam("prs_subcategory_id", String.class, updateProductIn.getSubcategoryId()),
                        ProcedureParameter.inputParam("prs_category_id", String.class, updateProductIn.getCategoryId()),
                        ProcedureParameter.inputParam("prs_product_status", Integer.class, updateProductIn.getStatus().value),
                        ProcedureParameter.inputParam("prs_image_links", String.class, imageUrlList),
                        ProcedureParameter.outputParam("out_result", String.class)
                )
        );
        String result = (String) outputs.get("out_result");
        if (DatabaseStatus.isExist(result)) throw new ApiErrorException(ApiError.fieldExist(result));
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("Update product failed!");

    }

    public void deleteProduct(String productId) {
        var outputs = procedureCallerV3.callNoRefCursor("product_delete", List.of(
                ProcedureParameter.inputParam("prs_product_id", String.class, productId),
                ProcedureParameter.outputParam("out_result", String.class))
        );
        String result = (String) outputs.get("out_result");
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("Delete product failed!");
    }

    public ProductDetailOut getProductDetail(String productId) {
        var outputs = procedureCallerV3.callOneRefCursor("product_detail",
                List.of(
                        ProcedureParameter.inputParam("prs_product_id", String.class, productId),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ),
                ProductDetailOut.class
        );
        List<ProductDetailOut> outList = (List<ProductDetailOut>) outputs.get("out_cur");
        if (outList == null || outList.isEmpty()) {
            throw new NotFoundException("Product not found!");
        }

        return outList.get(0);
    }

    public List<ProductDetailOut.TagProduct> getProductTags(String productId) {
        var outputs = procedureCallerV3.callOneRefCursor("tag_product_list",
                List.of(
                        ProcedureParameter.inputParam("prs_product_id", String.class, productId),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ),
                ProductDetailOut.TagProduct.class
        );
        List<ProductDetailOut.TagProduct> outList = (List<ProductDetailOut.TagProduct>) outputs.get("out_cur");
        
//        if (outList == null || outList.isEmpty()) {
//            throw new NotFoundException("tag_product_list not found!");
//        }

        return outList;
    }

    public PagingOut<ProductListOut> getAllProducts(SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("product_list_filter",
                List.of(
                        ProcedureParameter.inputParam("prs_properties_sort", String.class, sortPageIn.getPropertiesSort()),
                        ProcedureParameter.inputParam("prs_sort", String.class, sortPageIn.getSort()),
                        ProcedureParameter.inputParam("prn_page_index", Integer.class, sortPageIn.getPage()),
                        ProcedureParameter.inputParam("prn_page_size", Integer.class, sortPageIn.getMaxSize()),
                        ProcedureParameter.inputParam("prs_key_search", String.class, sortPageIn.getKeySearch()),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), ProductListOut.class
        );

        List<ProductListOut> outList = (List<ProductListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

    public PagingOut<ProductListOut> dynamicFilter(ProductFilterIn productFilterIn, SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("product_dynamic_filter",
                List.of(
                        ProcedureParameter.inputParam("prs_subcategory_ids", String.class, productFilterIn.getSubCategoriesList()),
                        ProcedureParameter.inputParam("prs_tag_ids", String.class, productFilterIn.getTagsList()),
                        ProcedureParameter.inputParam("prs_min_price", Long.class, productFilterIn.getMinPrice()),
                        ProcedureParameter.inputParam("prs_max_price", Long.class, productFilterIn.getMaxPrice()),
                        ProcedureParameter.inputParam("prs_status", Integer.class, productFilterIn.getProductStatusEnum().value),
                        ProcedureParameter.inputParam("prs_category_id", String.class, productFilterIn.getCategoryId()),
                        ProcedureParameter.inputParam("prs_properties_sort", String.class, sortPageIn.getPropertiesSort()),
                        ProcedureParameter.inputParam("prs_sort", String.class, sortPageIn.getSort()),
                        ProcedureParameter.inputParam("prn_page_index", Integer.class, sortPageIn.getPage()),
                        ProcedureParameter.inputParam("prn_page_size", Integer.class, sortPageIn.getMaxSize()),
                        ProcedureParameter.inputParam("prs_key_search", String.class, sortPageIn.getKeySearch()),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), ProductListOut.class
        );
        List<ProductListOut> outList = (List<ProductListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

    public PagingOut<ProductListOut> getAllProductBySubCategory(String subCategoryId, SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("product_subcategory_list",
                List.of(
                        ProcedureParameter.inputParam("prs_sub_category_id", String.class, subCategoryId),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), ProductListOut.class
        );

        List<ProductListOut> outList = (List<ProductListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

    public List<ProductAttributeOut> getProductAttribute(String productId) {
        var outputs = procedureCallerV3.callOneRefCursor("product_attribute_detail",
                List.of(
                        ProcedureParameter.inputParam("prs_product_id", String.class, productId),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ),
                ProductAttributeOut.class
        );

        return (List<ProductAttributeOut>) outputs.get("out_cur");
    }
}
