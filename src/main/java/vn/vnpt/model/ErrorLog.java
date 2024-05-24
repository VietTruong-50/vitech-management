package vn.vnpt.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorLog {
    private String id;
    private String errorDetail;
    private String errorType;
    private String timestamp;
}
