package vn.vnpt.common;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.service.helper.KafkaProducerService;
import vn.vnpt.common.constant.ConstantString;
import vn.vnpt.common.errorcode.ErrorCode;
import vn.vnpt.common.exception.IllegalArgumentException;
import vn.vnpt.common.exception.*;
import vn.vnpt.common.response.ResponseEntities;
import vn.vnpt.model.ErrorLog;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;

@Slf4j
public abstract class AbstractResponseController<T> {

	@Autowired
	private KafkaProducerService kafkaProducerService;

	protected AbstractResponseController() {
	}

	public DeferredResult<ResponseEntity<?>> responseEntityDeferredResult(CallbackFunction<T> callbackFunction) {
		Gson gson = new Gson();
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>(ConstantString.TIMEOUT_NONBLOCK);
		deferredResult.onTimeout(() -> deferredResult.setErrorResult(
				ResponseEntities.createErrorResponse(HttpStatus.REQUEST_TIMEOUT, ErrorCode.IDG_00000408)));
		try {
			deferredResult.setResult(ResponseEntities.createSuccessResponse(HttpStatus.OK, callbackFunction.execute()));
		} catch (NotFoundException ex) {
			handleException(ex, HttpStatus.NOT_FOUND, ex.getErrorCode(), ex.getMessage(), gson, deferredResult);
		} catch (AuthenticationException | IllegalArgumentException ex) {
			handleException(ex, HttpStatus.BAD_REQUEST, ErrorCode.IDG_00000401, "Tài khoản hoặc mật khẩu không chính xác", gson, deferredResult);
		} catch (AccessDeniedException ex) {
			handleException(ex, HttpStatus.NOT_ACCEPTABLE, ErrorCode.IDG_00000406, ex.getMessage().trim(), gson, deferredResult);
		} catch (ApiErrorException ex) {
			handleException(ex, ex.getApiError().getError(), gson, deferredResult);
		} catch (BadRequestException ex) {
			handleException(ex, HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage().trim(), gson, deferredResult);
		} catch (RuntimeException ex) {
			handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.IDG_00000500, ex.getMessage().trim(), gson, deferredResult);
		} catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return deferredResult;
	}

	private void handleException(Exception ex, HttpStatus httpStatus, String errorCode, String message, Gson gson, DeferredResult<ResponseEntity<?>> deferredResult) {
		log.error(ex.getMessage(), ex);
		deferredResult.setResult(ResponseEntities.createErrorResponse(httpStatus, errorCode, Common.subString(message)));
		kafkaProducerService.sendMessage("exception", gson.toJson(ErrorLog.builder()
				.errorType("GenericError")
				.errorDetail(ex.getMessage())
				.timestamp(Instant.now().toString())
				.build()));
	}

	private void handleException(Exception ex, String apiError, Gson gson, DeferredResult<ResponseEntity<?>> deferredResult) {
		log.error(ex.getMessage(), ex);
		deferredResult.setResult(ResponseEntities.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, apiError));
		kafkaProducerService.sendMessage("exception", gson.toJson(ErrorLog.builder()
				.errorType("ServerError")
				.errorDetail(ex.getMessage())
				.timestamp(Instant.now().toString())
				.build()));
	}
}