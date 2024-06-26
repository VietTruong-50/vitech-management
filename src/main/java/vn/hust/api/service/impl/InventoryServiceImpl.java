package vn.hust.api.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
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
        var folderId = driveService.createFolder(createProductIn.getName());
        var fileRes = driveService.uploadImageToDrive(file, folderId);

        List<String> imageUrlList = new ArrayList<>();
        if (!Common.isNullOrEmpty(images)) {
            for (var it : images) {
                var img = driveService.uploadImageToDrive(it, folderId);

                imageUrlList.add(img.getUrl());
            }
        }

        if (!Common.isNullOrEmpty(createProductIn.getTagIds())) {
            var listTag = tagRepository.listAllByCategory(createProductIn.getCategoryId());
            var newListTags = createProductIn.getTagIds();
            for (String tagId : newListTags) {
                boolean tagExists = false;
                for (TagListOut tag : listTag) {
                    if (tag.getTagId().equals(tagId)) {
                        tagExists = true;
                        break;
                    }
                }
                if (!tagExists) {
                    newListTags.remove(tagId);
                    var id = tagRepository.createNewTag(createProductIn.getCategoryId(), tagId);
                    newListTags.add(id);
                }
            }
            createProductIn.setTagIds(newListTags);
        }

        String productId = productRepository.createNewProduct(createProductIn, userId, fileRes.getUrl(), imageUrlList);

        if (!Common.isNullOrEmpty(createProductIn.getAttributes())) {
            for (String key : createProductIn.getAttributes().keySet()) {
                List<Map<String, Object>> attributeDetailsList = createProductIn.getAttributes().get(key);

                for (Map<String, Object> attributeDetails : attributeDetailsList) {
                    String value = (String) attributeDetails.get("value");
                    Long priceAddStr = !Common.isNullOrEmpty(attributeDetails.get("priceAdd")) ? (Long) attributeDetails.get("priceAdd") : 0;

                    productRepository.addAttribute(value, key, productId, priceAddStr);
                }
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

        if (!Common.isNullOrEmpty(updateProductIn.getAttributes())) {
            for (String key : updateProductIn.getAttributes().keySet()) {
                List<Map<String, Object>> attributeDetailsList = updateProductIn.getAttributes().get(key);

                for (Map<String, Object> attributeDetails : attributeDetailsList) {
                    String value = (String) attributeDetails.get("value");
                    Long priceAddStr = !Common.isNullOrEmpty(attributeDetails.get("priceAdd")) ? (Long) attributeDetails.get("priceAdd") : 0;

                    productRepository.addAttribute(value, key, updateProductIn.getProductId(), priceAddStr);
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
