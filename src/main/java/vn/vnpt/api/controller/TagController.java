package vn.vnpt.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.service.TagService;
import vn.vnpt.common.AbstractResponseController;
import vn.vnpt.common.model.SortPageIn;
@RestController
@RequestMapping("/v1/management/tag")
@RequiredArgsConstructor
@Slf4j
public class TagController extends AbstractResponseController {

    @Autowired
    private TagService tagService;

    @GetMapping(value = "/list", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> listTag(SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/shopping/tag/list");
            var rs = tagService.listTag(sortPageIn);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

}
