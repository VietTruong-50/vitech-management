package vn.vnpt.api.service.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class CategoryTreeBuilder {
    private static Map<String, CategoryOut> categoryMap;

    public CategoryTreeBuilder(List<CategoryOut> categories) {
        categoryMap = new HashMap<>();
        for (CategoryOut category : categories) {
            categoryMap.put(category.getCategoryId(), category);
        }
    }

    public static CategoryOut buildCategoryTree() {
        CategoryOut root = new CategoryOut("root", "Root", null);
        for (CategoryOut category : categoryMap.values()) {
            if (category.getParentIds() == null || category.getParentIds().isEmpty()) {
                root.getChildren().add(category);
            } else {
                for (String parentId : category.getParentIds()) {
                    CategoryOut parent = categoryMap.get(parentId.trim());
                    if (parent != null) {
                        parent.getChildren().add(category);
                    }
                }
            }
        }
        return root;
    }

    // Method to print category tree (for testing)
    public void printCategoryTree(CategoryOut category, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("\t");
        }
        sb.append(category.getCategoryName());
        for (CategoryOut child : category.getChildren()) {
            printCategoryTree(child, level + 1);
        }
    }
}
