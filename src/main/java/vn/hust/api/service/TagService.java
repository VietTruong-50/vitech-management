package vn.hust.api.service;

import vn.hust.api.dto.out.tag.TagListOut;

import java.util.List;

public interface TagService {
    List<TagListOut> listTag(String categoryId);
}
