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

import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.json.bind.serializer.JsonbDeserializer;

/**
 * Common interface for all the customizations.
 */
public interface JsonbCustomization {

    /**
     * Return {@link NumberFormat} specified to the required {@link Scope}.
     *
     * @param scope required scope
     * @return specified {@link NumberFormat} instance, otherwise empty
     */
    Optional<NumberFormat> numberFormat(Scope scope);

    /**
     * Return {@link DateFormat} specified to the required {@link Scope}.
     *
     * @param scope required scope
     * @return specified {@link DateFormat} instance, otherwise empty
     */
    Optional<DateFormat> dateFormat(Scope scope);

    /**
     * Return {@link JsonbDeserializer} of the component.
     *
     * @return component deserializer instance, otherwise empty
     */
    Optional<JsonbDeserializer<?>> deserializer();

    /**
     * Return {@link JsonbAdapter} of the component.
     *
     * @return component adapter instance, otherwise empty
     */
    Optional<JsonbAdapter<?, ?>> adapter();

    /**
     * Return if the component can be nillable in the given {@link Scope}.
     *
     * @param scope required scope
     * @return property nillable state, otherwise empty
     */
    Optional<Boolean> nillable(Scope scope);

}
