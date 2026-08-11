/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

package ee.jakarta.tck.json.bind.customizedmapping.instantiation.model;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * Used to verify that a custom constructor used by @JsonbCreator
 * can be used to customize fields and that primitive fields not 
 * supplied to the constructor are defaulted based on the specification.
 */
public record SimpleCreatorPlusFieldsRecord(String stringInstance, Integer integerInstance, float floatInstance) {

    @JsonbCreator
    public static SimpleCreatorPlusFieldsRecord create(
            @JsonbProperty("stringInstance") String stringInstance,
            @JsonbProperty("integerInstance") Integer integerInstance) {
        // supplied values are intentionally ignored for testing
        return new SimpleCreatorPlusFieldsRecord("Constructor String", 2, 0f);
    }
}
