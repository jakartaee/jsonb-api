/*
 * Copyright (c) 2017, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */

package jakarta.json.bind.tck.api.exception;

import jakarta.json.bind.JsonbException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * @test
 * @sources JsonbExceptionTest.java
 * @executeClass com.sun.ts.tests.jsonb.api.JsonbExceptionTest
 **/
public class JsonbExceptionTest {

    private static final String EXCEPTION_MESSAGE = "Exception message";

    /*
     * @testName: testJsonbExceptionString
     *
     * @assertion_ids: JSONB:JAVADOC:51
     *
     * @test_Strategy: Assert that JsonbException with String argument is creating
     * a new RuntimeException with cause uninitialized and that subsequent call to
     * #initCause may initialize the cause
     */
    @Test
    public void testJsonbExceptionString() {
        RuntimeException jsonbException = new JsonbException(EXCEPTION_MESSAGE);
        String validationMessage = "Failed to create JsonbException with an exception message and empty cause.";
        assertThat(validationMessage, jsonbException.getMessage(), is(EXCEPTION_MESSAGE));
        assertThat(validationMessage, jsonbException.getCause(), nullValue());

        RuntimeException exception = new RuntimeException();
        jsonbException.initCause(exception);
        validationMessage = "Failed to initialize the JsonbException cause with a call to initCause method.";
        assertThat(validationMessage, jsonbException.getMessage(), is(EXCEPTION_MESSAGE));
        assertThat(validationMessage, jsonbException.getCause(), is(exception));
    }

    /*
     * @testName: testJsonbExceptionStringThrowable
     *
     * @assertion_ids: JSONB:JAVADOC:52
     *
     * @test_Strategy: Assert that JsonbException with String and throwable
     * arguments is creating a new RuntimeException with exception message and
     * cause initialized
     */
    @Test
    public void testJsonbExceptionStringThrowable() {
        RuntimeException cause = new RuntimeException();
        RuntimeException jsonbException = new JsonbException(EXCEPTION_MESSAGE, cause);
        String validationMessage = "Failed to create JsonbException with an exception message and cause.";
        assertThat(validationMessage, jsonbException.getMessage(), is(EXCEPTION_MESSAGE));
        assertThat(validationMessage, jsonbException.getCause(), is(cause));
    }
}
