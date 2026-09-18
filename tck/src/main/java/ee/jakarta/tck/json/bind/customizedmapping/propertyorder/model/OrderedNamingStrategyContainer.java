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

package ee.jakarta.tck.json.bind.customizedmapping.propertyorder.model;

import jakarta.json.bind.annotation.JsonbPropertyOrder;

/**
 * Class annotated with {@code @JsonbPropertyOrder} using the original field names.
 * When combined with a {@code PropertyNamingStrategy}, the annotation values must
 * still use the original identity names, while the output uses strategy-transformed names.
 *
 * <p>Fields: {@code longInstance}, {@code intInstance}, {@code stringInstance}.
 * With {@code LOWER_CASE_WITH_DASHES} strategy these become:
 * {@code long-instance}, {@code int-instance}, {@code string-instance}.
 * Ordering in the annotation uses the original names.
 */
@JsonbPropertyOrder({"longInstance", "intInstance", "stringInstance"})
public class OrderedNamingStrategyContainer {

    private int intInstance;

    private String stringInstance;

    private long longInstance;

    public int getIntInstance() {
        return intInstance;
    }

    public void setIntInstance(int intInstance) {
        this.intInstance = intInstance;
    }

    public String getStringInstance() {
        return stringInstance;
    }

    public void setStringInstance(String stringInstance) {
        this.stringInstance = stringInstance;
    }

    public long getLongInstance() {
        return longInstance;
    }

    public void setLongInstance(long longInstance) {
        this.longInstance = longInstance;
    }
}
