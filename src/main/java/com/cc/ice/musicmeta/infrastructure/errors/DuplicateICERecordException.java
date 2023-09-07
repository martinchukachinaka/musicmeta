package com.cc.ice.musicmeta.infrastructure.errors;

public class DuplicateICERecordException extends IllegalStateException {

	public DuplicateICERecordException(String message) {
		super(message);
	}
}
