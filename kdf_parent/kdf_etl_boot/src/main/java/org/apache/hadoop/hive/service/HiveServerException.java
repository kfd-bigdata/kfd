package org.apache.hadoop.hive.service;

import javax.annotation.Nullable;

/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Root of the hierarchy of data access exceptions discussed in
 * <a href="https://www.amazon.com/exec/obidos/tg/detail/-/0764543857/">Expert
 * One-On-One J2EE Design and Development</a>. Please see Chapter 9 of this book
 * for detailed discussion of the motivation for this package.
 *
 * <p>
 * This exception hierarchy aims to let user code find and handle the kind of
 * error encountered without knowing the details of the particular data access
 * API in use (e.g. JDBC). Thus it is possible to react to an optimistic locking
 * failure without knowing that JDBC is being used.
 *
 * <p>
 * As this class is a runtime exception, there is no need for user code to catch
 * it or subclasses if any error is to be considered fatal (the usual case).
 *
 * @author java男神
 */
@SuppressWarnings("serial")
public abstract class HiveServerException extends RuntimeException {

	/**
	 * Constructor for HiveServerException .
	 * 
	 * @param msg the detail message
	 */
	public HiveServerException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for HiveServerException.
	 * 
	 * @param msg   the detail message
	 * @param cause the root cause (usually from using a underlying data access API
	 *              such as JDBC)
	 */
	public HiveServerException(@Nullable String msg, @Nullable Throwable cause) {
		super(msg, cause);
	}

}
