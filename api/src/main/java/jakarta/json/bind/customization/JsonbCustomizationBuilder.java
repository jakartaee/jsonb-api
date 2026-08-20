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
import java.util.Locale;

import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.json.bind.serializer.JsonbDeserializer;

/**
 * Common interface for all customization builders.
 */
public interface JsonbCustomizationBuilder<T extends JsonbCustomizationBuilder<T, B>, B extends JsonbCustomization> {

    /**
     * Whether this component can be nillable.
     *
     * @param nillable nillable component
     * @return updated builder instance
     */
    T nillable(boolean nillable);

    /**
     * Ignore nillable customization of this component.
     *
     * @return updated builder instance
     */
    T ignoreNillable();

    /**
     * Set {@link JsonbDeserializer} which should be used.
     *
     * @param deserializer component deserializer
     * @return updated builder instance
     */
    T deserializer(JsonbDeserializer<?> deserializer);

    /**
     * Ignore deserializer of this component.
     *
     * @return updated builder instance
     */
    T ignoreDeserializer();

    /**
     * Set {@link JsonbAdapter} which should be used.
     *
     * @param adapter component adapter
     * @return updated builder instance
     */
    T adapter(JsonbAdapter<?, ?> adapter);

    /**
     * Ignore adapter of this component.
     *
     * @return updated builder instance
     */
    T ignoreAdapter();

    /**
     * Set number format and locale which should be used.
     *
     * @param numberFormat number format
     * @param locale locale
     * @return updated builder instance
     */
    T numberFormat(String numberFormat, Locale locale);

    /**
     * Set number format which should be used.
     *
     * @param numberFormat number format
     * @return updated builder instance
     */
    T numberFormat(String numberFormat);

    /**
     * Set locale which should be used.
     *
     * @param locale locale
     * @return updated builder instance
     */
    T numberFormat(Locale locale);

    /**
     * Set {@link NumberFormat} instance which should be used.
     *
     * @param numberFormat pre created NumberFormat instance
     * @return updated builder instance
     */
    T numberFormat(NumberFormat numberFormat);

    /**
     * Ignore number format customization of this component.
     *
     * @return updated builder instance
     */
    T ignoreNumberFormat();

    /**
     * Set date format and locale which should be used.
     *
     * @param dateFormat date format
     * @param locale locale
     * @return updated builder instance
     */
    T dateFormat(String dateFormat, Locale locale);

    /**
     * Set date format which should be used.
     *
     * @param dateFormat date format
     * @return updated builder instance
     */
    T dateFormat(String dateFormat);

    /**
     * Set locale which should be used.
     *
     * @param locale locale
     * @return updated builder instance
     */
    T dateFormat(Locale locale);

    /**
     * Set {@link DateFormat} instance which should be used.
     *
     * @param dateFormat pre created DateFormat instance
     * @return updated builder instance
     */
    T dateFormat(DateFormat dateFormat);

    /**
     * Ignore date format customization of this component.
     *
     * @return updated builder instance
     */
    T ignoreDateFormat();

    /**
     * Ignore all the customizations.
     *
     * @return updated builder instance
     */
    T ignoreAllCustomizations();

    /**
     * Build the new instance from this builder.
     *
     * @return new instance
     */
    B build();

}
