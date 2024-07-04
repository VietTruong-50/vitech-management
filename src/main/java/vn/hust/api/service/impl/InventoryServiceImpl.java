package vn.hust.api.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.hust.api.dto.in.CreateProductIn;
import vn.hust.api.dto.in.ProductFilterIn;
import vn.hust.api.dto.in.UpdateProductIn;
import vn.hust.api.dto.out.product.ProductAttributeOut;
import vn.hust.api.dto.out.product.ProductDetailOut;
import vn.hust.api.dto.out.product.ProductListOut;
import vn.hust.api.dto.out.product.attribute.AttributeListOut;
import vn.hust.api.dto.out.product.attribute.ColorInfoOut;
import vn.hust.api.dto.out.tag.TagListOut;
import vn.hust.api.repository.CategoryRepository;
import vn.hust.api.repository.ProductRepository;
import vn.hust.api.repository.TagRepository;
import vn.hust.api.service.DriveService;
import vn.hust.api.service.InventoryService;
import vn.hust.common.Common;
import vn.hust.common.exception.BadRequestException;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final DriveService driveService;
    private final ObjectMapper objectMapper;

    @Override
    public void createNewProduct(CreateProductIn createProductIn, MultipartFile file, MultipartFile[] images) {
        var jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = jwt.getClaims().get("sub").toString();
        log.info("Starting product creation for user: {}", userId);

        var folderId = driveService.createFolder(createProductIn.getName());
        log.info("Created Google Drive folder with ID: {}", folderId);

        var fileRes = driveService.uploadImageToDrive(file, folderId);
        log.info("Uploaded main image to Drive with URL: {}", fileRes.getUrl());

        List<String> imageUrlList = new ArrayList<>();
        imageUrlList.add(fileRes.getUrl());

        if (!Common.isNullOrEmpty(images)) {
            log.info("Uploading additional images...");
            for (var it : images) {
                var img = driveService.uploadImageToDrive(it, folderId);
                log.info("Uploaded additional image to Drive with URL: {}", img.getUrl());

                imageUrlList.add(img.getUrl());
            }
        }

        if (!Common.isNullOrEmpty(createProductIn.getTagIds())) {
            log.info("Processing tags for the product...");
            var listTag = tagRepository.listAllByCategory(createProductIn.getCategoryId());
            var productTagsId = createProductIn.getTagIds();

            for (String tagId : productTagsId) {
                var existed = listTag.stream().map(TagListOut::getTagId).toList().contains(tagId) || listTag.stream().map(TagListOut::getTagName).toList().contains(tagId);

                if (!existed) {
                    log.info("Creating new tag with id: {}", tagId);
                    var id = tagRepository.createNewTag(createProductIn.getCategoryId(), tagId);
                    productTagsId.add(id);
                }
            }
            createProductIn.setTagIds(productTagsId);
        }

        String productId = productRepository.createNewProduct(createProductIn, userId, imageUrlList.get(0), imageUrlList);
        log.info("Created new product with ID: {}", productId);

        if (!Common.isNullOrEmpty(createProductIn.getAttributes())) {
            log.info("Adding attributes to the product...");
            for (String key : createProductIn.getAttributes().keySet()) {
                List<Map<String, Object>> attributeDetailsList = createProductIn.getAttributes().get(key);

                for (Map<String, Object> attributeDetails : attributeDetailsList) {
                    String value = (String) attributeDetails.get("value");
                    Long priceAddStr = !Common.isNullOrEmpty(attributeDetails.get("priceAdd")) ? (Long) attributeDetails.get("priceAdd") : 0;
                    log.info("Adding attribute with key: {}, value: {}, priceAdd: {}", key, value, priceAddStr);

                    productRepository.addAttribute(value, key, productId, priceAddStr);
                }
            }
        }

        log.info("Product creation completed for user: {}", userId);
    }

    @Override
    public void updateProduct(UpdateProductIn updateProductIn, MultipartFile file, MultipartFile[] images, String featureImageChange) {
        var productDetail = productRepository.getProductDetail(updateProductIn.getProductId());

        var list = Arrays.stream(productDetail.getImageLinks().replaceAll("\\[", "")
                        .replaceAll("\\]", "").split(",")).map(String::trim)
                .collect(Collectors.toList());

        String folderId = driveService.checkFolderExists(updateProductIn.getName());

        if (!Common.isNullOrEmpty(folderId)) {
            driveService.renameFolder(updateProductIn.getName(), folderId);

//            //delete old folder
//            String oldFolderId = driveService.checkFolderExists(productDetail.getName());
//            if (Common.isNullOrEmpty(oldFolderId)) driveService.deleteFolder(oldFolderId);
        } else {
            throw new BadRequestException("folder exist");
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

        if (!Common.isNullOrEmpty(updateProductIn.getAttributes())) {
            log.info("Adding attributes to the product...");
            for (String key : updateProductIn.getAttributes().keySet()) {
                List<Map<String, Object>> attributeDetailsList = updateProductIn.getAttributes().get(key);

                for (Map<String, Object> attributeDetails : attributeDetailsList) {
                    String value = (String) attributeDetails.get("value");
                    Long priceAddStr = !Common.isNullOrEmpty(attributeDetails.get("priceAdd")) ? (Long) attributeDetails.get("priceAdd") : 0;
                    log.info("Adding attribute with key: {}, value: {}, priceAdd: {}", key, value, priceAddStr);

                    productRepository.addAttribute(value, key, productDetail.getId(), priceAddStr);
                }
            }
        }
    }

    @Override
    public void deleteProduct(String productId) {
        var productDetail = getProductDetail(productId);
        var folderId = driveService.checkFolderExists(productDetail.getName());

        if (Common.isNullOrEmpty(folderId)) {
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
            var attributes = productRepository.getProductAttribute(productId);

            Map<String, List<ProductAttributeOut>> groupedAttributes = new HashMap<>();
            for (ProductAttributeOut attribute : attributes) {
                String name = attribute.getName();
                if (!groupedAttributes.containsKey(name)) {
                    groupedAttributes.put(name, new ArrayList<>());
                }
                groupedAttributes.get(name).add(attribute);
            }

            rs.setAttributes(groupedAttributes);
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
    public List<AttributeListOut> getAllAttribute(String categoryId) {
        var category = categoryRepository.getCategoryDetail(categoryId);

        var ids = Stream.of(category.getAttributeIds().split(",")).map(String::trim).toList();

        var rs = productRepository.getAllAttribute();
        ObjectMapper objectMapper = new ObjectMapper();

        return rs.stream()
                .filter(attribute -> ids.contains(attribute.getAttributeId()))
                .peek(attribute -> {
                    if (!attribute.getName().contains("Color")) {
                        attribute.setValueList(Stream.of(attribute.getValues().split(","))
                                .map(String::trim)
                                .collect(Collectors.toList()));
                    } else {
                        try {
                            List<ColorInfoOut> colorList = objectMapper.readValue(attribute.getValues(), new TypeReference<>() {
                            });
                            attribute.setValueList(colorList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .collect(Collectors.toList());
    }
}
