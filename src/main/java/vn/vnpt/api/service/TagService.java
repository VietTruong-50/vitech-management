package vn.vnpt.api.service;

import vn.vnpt.api.dto.out.tag.TagListOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.List;

public interface TagService {
    List<TagListOut> listTag(SortPageIn sortPageIn);
}
