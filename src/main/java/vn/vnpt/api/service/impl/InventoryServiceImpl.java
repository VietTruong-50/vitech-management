package vn.vnpt.api.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.vnpt.api.dto.in.CreateProductIn;
import vn.vnpt.api.dto.in.ProductFilterIn;
import vn.vnpt.api.dto.in.UpdateProductIn;
import vn.vnpt.api.dto.out.product.ProductDetailOut;
import vn.vnpt.api.dto.out.product.ProductListOut;
import vn.vnpt.api.dto.out.product.attribute.AttributeListOut;
import vn.vnpt.api.dto.out.product.attribute.ColorInfoOut;
import vn.vnpt.api.repository.ProductRepository;
import vn.vnpt.api.service.DriveService;
import vn.vnpt.api.service.InventoryService;
import vn.vnpt.common.Common;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final ProductRepository productRepository;
    private final DriveService driveService;
    private final ObjectMapper objectMapper;

    @Override
    public void createNewProduct(CreateProductIn createProductIn, MultipartFile file, MultipartFile[] images) {
        var jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = jwt.getClaims().get("sub").toString();
        var folderId = driveService.createFolder(createProductIn.getName());
        var fileRes = driveService.uploadImageToDrive(file, folderId);

        List<String> imageUrlList = new ArrayList<>();
        if (!Common.isNullOrEmpty(images)) {
            for (var it : images) {
                var img = driveService.uploadImageToDrive(it, folderId);

                imageUrlList.add(img.getUrl());
            }
        }

        String productId = productRepository.createNewProduct(createProductIn, userId, fileRes.getUrl(), imageUrlList);

        for (String key : createProductIn.getAttributes().keySet()) {
            List<String> values = createProductIn.getAttributes().get(key);

            for (String value : values) {
                productRepository.addAttribute(value, key, productId);
            }
        }
    }

    @Override
    public void updateProduct(UpdateProductIn updateProductIn, MultipartFile file, MultipartFile[] images) {
        var productDetail = productRepository.getProductDetail(updateProductIn.getProductId());

        var list = Arrays.stream(productDetail.getImageLinks().replaceAll("\\[", "")
                        .replaceAll("\\]", "").split(",")).map(String::trim)
                .collect(Collectors.toList());

        String folderId = driveService.checkFolderExists(updateProductIn.getName());

        if (!Common.isNullOrEmpty(folderId)) {
            folderId = driveService.createFolder(updateProductIn.getName());

            //delete old folder
            String oldFolderId = driveService.checkFolderExists(productDetail.getName());
            if (Common.isNullOrEmpty(oldFolderId)) driveService.deleteFolder(oldFolderId);
        }

        if (!updateProductIn.getImageDelete().isEmpty()) {
            for (var image : updateProductIn.getImageDelete()) {
                list.remove(image);
                driveService.deleteImage(image.replace("https://drive.google.com/thumbnail?id=", ""));
            }
        }

        var fileRes = driveService.uploadImageToDrive(file, folderId);

        if (!Common.isNullOrEmpty(images)) {
            for (var it : images) {
                var img = driveService.uploadImageToDrive(it, folderId);

                list.add(img.getUrl());
            }
        }

        productRepository.updateProduct(updateProductIn, fileRes.getUrl(), list);

        for (String key : updateProductIn.getAttributes().keySet()) {
            List<String> values = updateProductIn.getAttributes().get(key);

            for (String value : values) {
                productRepository.updateAttribute(value, key, updateProductIn.getProductId());
            }
        }
    }

    @Override
    public void deleteProduct(String productId) {
        var productDetail = getProductDetail(productId);
        var folderId = driveService.checkFolderExists(productDetail.getName());

        if(Common.isNullOrEmpty(folderId)){
            driveService.deleteFolder(folderId);
        }

        productRepository.deleteProduct(productId);
    }

    @Override
    public PagingOut<ProductListOut> listAllProducts(SortPageIn sortPageIn) {
        return productRepository.getAllProducts(sortPageIn);
    }

    @Override
    public ProductDetailOut getProductDetail(String productId) {
        var rs = productRepository.getProductDetail(productId);

        try {
            rs.setParameters(objectMapper.readValue(rs.getParametersJson(), new TypeReference<>() {
            }));
            rs.setTagProducts(productRepository.getProductTags(productId));
//            kafkaProducerService.sendMessage("", rs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rs;
    }

    @Override
    public PagingOut<ProductListOut> filterProducts(ProductFilterIn productFilterIn, SortPageIn sortPageIn) {
        return productRepository.dynamicFilter(productFilterIn, sortPageIn);
    }

    @Override
    public PagingOut<ProductListOut> findAllBySubCategory(String subCategoryId, SortPageIn sortPageIn) {
        return productRepository.getAllProductBySubCategory(subCategoryId, sortPageIn);
    }

    @Override
    public List<AttributeListOut> getAllAttribute() {
        var rs = productRepository.getAllAttribute();
        ObjectMapper objectMapper = new ObjectMapper();

        return rs.stream()
                .peek(attribute -> {
                    if (!Objects.equals(attribute.getName(), "Màu sắc")) {
                        attribute.setValueList(Stream.of(attribute.getValues().split(","))
                                .map(String::trim)
                                .collect(Collectors.toList()));
                    } else {
                        try {
                            List<ColorInfoOut> colorList = objectMapper.readValue(attribute.getValues(), new TypeReference<List<ColorInfoOut>>() {});
                            attribute.setValueList(colorList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .collect(Collectors.toList());
    }
}
