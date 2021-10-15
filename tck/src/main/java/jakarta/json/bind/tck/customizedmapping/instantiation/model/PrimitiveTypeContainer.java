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

package jakarta.json.bind.tck.customizedmapping.instantiation.model;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class PrimitiveTypeContainer {

    private final byte byteType;
    private final short shortType;
    private final int intType;
    private final long longType;
    private final float floatType;
    private final double doubleType;
    private final char charType;
    private final boolean booleanType;

    public PrimitiveTypeContainer(byte byteType,
                                  short shortType,
                                  int intType,
                                  long longType,
                                  float floatType,
                                  double doubleType,
                                  char charType,
                                  boolean booleanType) {
        this.byteType = byteType;
        this.shortType = shortType;
        this.intType = intType;
        this.longType = longType;
        this.floatType = floatType;
        this.doubleType = doubleType;
        this.charType = charType;
        this.booleanType = booleanType;
    }

    @JsonbCreator
    public PrimitiveTypeContainer create(@JsonbProperty("byteType") byte byteType,
                                         @JsonbProperty("shortType") short shortType,
                                         @JsonbProperty("intType") int intType,
                                         @JsonbProperty("longType") long longType,
                                         @JsonbProperty("floatType") float floatType,
                                         @JsonbProperty("doubleType") double doubleType,
                                         @JsonbProperty("charType") char charType,
                                         @JsonbProperty("booleanType") boolean booleanType) {
        return new PrimitiveTypeContainer(byteType, shortType, intType, longType, floatType, doubleType, charType, booleanType);
    }

    public byte getByteType() {
        return byteType;
    }

    public short getShortType() {
        return shortType;
    }

    public int getIntType() {
        return intType;
    }

    public long getLongType() {
        return longType;
    }

    public float getFloatType() {
        return floatType;
    }

    public double getDoubleType() {
        return doubleType;
    }

    public char getCharType() {
        return charType;
    }

    public boolean getBooleanType() {
        return booleanType;
    }
}
