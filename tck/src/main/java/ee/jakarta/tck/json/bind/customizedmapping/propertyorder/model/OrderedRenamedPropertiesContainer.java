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

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

/**
 * Class annotated with {@code @JsonbPropertyOrder} using the <em>original</em> field names,
 * while each field also carries a {@code @JsonbProperty} that renames it.
 *
 * <p>The annotation lists {@code ["longInstance", "intInstance", "stringInstance"]} — the
 * original identity names — even though the serialized JSON keys will be the renamed values.
 * This verifies that ordering is keyed on original names and the renamed keys appear in the output.
 */
@JsonbPropertyOrder({"longInstance", "intInstance", "stringInstance"})
public class OrderedRenamedPropertiesContainer {

    @JsonbProperty("alpha")
    private int intInstance;

    @JsonbProperty("beta")
    private String stringInstance;

    @JsonbProperty("gamma")
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
