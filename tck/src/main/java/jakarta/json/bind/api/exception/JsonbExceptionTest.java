/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.json.bind.api.exception;

import static org.junit.Assert.fail;

import org.junit.Test;

import jakarta.json.bind.JsonbException;

/**
 * @test
 * @sources JsonbExceptionTest.java
 * @executeClass com.sun.ts.tests.jsonb.api.JsonbExceptionTest
 **/
public class JsonbExceptionTest {

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
    RuntimeException jsonbException = new JsonbException("Exception message");
    if (!"Exception message".equals(jsonbException.getMessage())
        || jsonbException.getCause() != null) {
      fail(
          "Failed to create JsonbException with an exception message and empty cause.");
    }

    RuntimeException exception = new RuntimeException();
    jsonbException.initCause(exception);
    if (!"Exception message".equals(jsonbException.getMessage())
        || jsonbException.getCause() != exception) {
      fail(
          "Failed to initialize the JsonbException cause with a call to initCause method.");
    }

    return; // passed
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
    RuntimeException jsonbException = new JsonbException("Exception message",
        cause);
    if (!"Exception message".equals(jsonbException.getMessage())
        || jsonbException.getCause() != cause) {
      fail(
          "Failed to create JsonbException with an exception message and cause.");
    }

    return; // passed
  }
}
