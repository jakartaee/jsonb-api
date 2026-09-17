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
 * Record annotated with {@code @JsonbPropertyOrder} using the original component names,
 * while each component also carries a {@code @JsonbProperty} that renames it.
 *
 * <p>The annotation lists {@code ["longInstance", "intInstance", "stringInstance"]} — the
 * original identity names — even though the serialized JSON keys will be the renamed values.
 */
@JsonbPropertyOrder({"longInstance", "intInstance", "stringInstance"})
public record NamingStrategyOrderedRecord(
        @JsonbProperty("alpha") int intInstance,
        @JsonbProperty("beta") String stringInstance,
        @JsonbProperty("gamma") long longInstance) {
}
