package vn.vnpt.api.service;

import vn.vnpt.api.dto.out.tag.TagListOut;

import java.util.List;

public interface TagService {
    List<TagListOut> listTag(String categoryId);
}
