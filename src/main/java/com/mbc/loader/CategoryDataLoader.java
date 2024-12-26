package com.mbc.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbc.entity.Category;
import com.mbc.repository.CategoryRepository;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CategoryDataLoader {

    private final CategoryRepository categoryRepository;

    public CategoryDataLoader(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadCategoryData() throws Exception {
        // JSON 파일 경로
        File jsonFile = new File("src/main/resources/categories.json");

        // JSON 파일 읽기
        ObjectMapper objectMapper = new ObjectMapper();
        List<Category> categories = objectMapper.readValue(jsonFile, objectMapper.getTypeFactory().constructCollectionType(List.class, Category.class));

        // 카테고리 저장을 위한 임시 맵 (id -> Category)
        Map<Long, Category> categoryMap = new HashMap<>();

        // 부모-자식 관계 설정
        for (Category category : categories) {
            if (category.getParent() != null) {
                Long parentId = category.getParent().getId();
                Category parentCategory = categoryMap.get(parentId);
                category.setParent(parentCategory);
            }

            // 카테고리 저장 및 맵에 추가
            Category savedCategory = categoryRepository.save(category);
            categoryMap.put(savedCategory.getId(), savedCategory);
            System.out.println("카테고리 '" + category.getName() + "'가 저장되었습니다.");
        }
    }
}
