package vn.hust.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hust.api.dto.out.tag.TagListOut;
import vn.hust.api.repository.TagRepository;
import vn.hust.api.service.TagService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    @Override
    public List<TagListOut> listTag(String categoryId) {
        return tagRepository.listAllByCategory(categoryId);
    }
}
