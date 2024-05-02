package vn.vnpt.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.out.tag.TagListOut;
import vn.vnpt.api.repository.TagRepository;
import vn.vnpt.api.service.TagService;
import vn.vnpt.common.model.SortPageIn;

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
