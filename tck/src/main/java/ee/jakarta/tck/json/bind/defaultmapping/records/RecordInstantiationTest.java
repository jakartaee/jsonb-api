/*
 * Copyright (c) 2026 Eclipse and/or its affiliates. All rights reserved.
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
package ee.jakarta.tck.json.bind.defaultmapping.records;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RecordInstantiationTest {

    public static record RecordWithCanonicalConstructor(String a, int b) {
        public RecordWithCanonicalConstructor(String a, int b) {
            this.a = a.toUpperCase();
            this.b = b;
        }
    }

    public static record RecordWithCompactConstructor(String a, int b) {
        public RecordWithCompactConstructor {
            if (b < 0) throw new IllegalArgumentException("b cannot be negative");
        }
    }

    private final Jsonb jsonb = JsonbBuilder.create();

    /**
     * Verify that the overwritten canonical constructor is used 
     * by asserting that the string is all uppercase.
     */
    @Test
    public void testRecordCanonicalConstructor() {
        RecordWithCanonicalConstructor result = jsonb.fromJson("{\"a\":\"hello\",\"b\":1}", RecordWithCanonicalConstructor.class);
        assertThat("Expected canonical constructor to uppercase the string value.",
                   result.a(), is("HELLO"));
        assertThat("Expected integer component to be mapped correctly.",
                   result.b(), is(1));
    }

    /**
     * Verify that the compact constructor is used by catching an
     * expected exception.
     */
    @Test
    public void testRecordCompactConstructor() {
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{\"a\":\"hello\",\"b\":-1}", RecordWithCompactConstructor.class),
                     "Expected a JsonbException when deserializing a record whose compact constructor "
                             + "throws IllegalArgumentException for a negative value.");
    }

}