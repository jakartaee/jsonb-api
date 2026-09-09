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

package jakarta.json.bind.customization;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Optional;

/**
 * Customization methods without the scope.
 */
public interface ScopelessCustomization extends JsonbCustomization {

    /**
     * Return specified {@link NumberFormat}.
     *
     * @return specified {@link NumberFormat} instance, otherwise empty
     */
    Optional<NumberFormat> getNumberFormat();

    /**
     * Return {@link DateFormat}.
     *
     * @return specified {@link DateFormat} instance, otherwise empty
     */
    Optional<DateFormat> getDateFormat();

    /**
     * Return whether this component can be nillable.
     * If no explicit value has been set, empty optional is returned.
     *
     * @return property nillable state, if not explicitly set empty is returned
     */
    Optional<Boolean> getNillable();


}
