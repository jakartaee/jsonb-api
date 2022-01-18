/*
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.json.bind.customizedmapping.instantiation.model;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class OptionalTypeContainer {

    private final String stringOptional;
    private final int intOptional;
    private final long longOptional;
    private final double doubleOptional;

    public OptionalTypeContainer(String stringOptional, int intOptional, long longOptional, double doubleOptional) {
        this.stringOptional = stringOptional;
        this.intOptional = intOptional;
        this.longOptional = longOptional;
        this.doubleOptional = doubleOptional;
    }

    @JsonbCreator
    public static OptionalTypeContainer create(@JsonbProperty("stringOptional") Optional<String> stringOptional,
                                               @JsonbProperty("intOptional") OptionalInt intOptional,
                                               @JsonbProperty("longOptional") OptionalLong longOptional,
                                               @JsonbProperty("doubleOptional") OptionalDouble doubleOptional) {
        return new OptionalTypeContainer(stringOptional.orElse(null),
                                         intOptional.orElse(-1),
                                         longOptional.orElse(-1),
                                         doubleOptional.orElse(-1));
    }

    public String getStringOptional() {
        return stringOptional;
    }

    public int getIntOptional() {
        return intOptional;
    }

    public long getLongOptional() {
        return longOptional;
    }

    public double getDoubleOptional() {
        return doubleOptional;
    }
}
