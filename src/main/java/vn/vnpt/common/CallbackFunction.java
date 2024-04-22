package vn.vnpt.common;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface CallbackFunction<T> {
	T execute() throws GeneralSecurityException, IOException;
}
