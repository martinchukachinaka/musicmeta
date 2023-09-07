package com.cc.ice.musicmeta.infrastructure.errors;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.cc.ice.musicmeta.infrastructure.AppConstants.BAD_PAYLOAD;
import static com.cc.ice.musicmeta.infrastructure.AppConstants.DUPLICATE_RECORD;
import static com.cc.ice.musicmeta.infrastructure.AppConstants.NO_RECORD;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class GlobalErrorHandler {
	@ResponseStatus(BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ApiError methodArgumentNotValidException(Exception ex) {
		log.error("Error occurred while preparing payload", ex);
		return new ApiError(BAD_PAYLOAD);
	}

	@ResponseStatus(NOT_FOUND)
	@ResponseBody
	@ExceptionHandler(NoICERecordException.class)
	public ApiError recordNotFoundException(Exception ex) {
		log.error("Error working with record", ex);
		return new ApiError(NO_RECORD);
	}


	@ResponseStatus(CONFLICT)
	@ResponseBody
	@ExceptionHandler(DuplicateICERecordException.class)
	public ApiError duplicateRecordException(Exception ex) {
		log.error("Error found", ex);
		return new ApiError(DUPLICATE_RECORD);
	}
}
