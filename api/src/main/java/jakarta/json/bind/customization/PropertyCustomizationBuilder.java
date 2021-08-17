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

import jakarta.json.bind.annotation.JsonbDateFormat;

/**
 * Builder of the specific class property customization.
 * <br>
 * Customization of the property accessor methods is done over the methods with the {@link Scope}
 * parameter. Methods without any specific {@link Scope} parameter are setting required customization
 * to the deserialization and serialization.
 * <p>
 * For example having getter method with custom name like this
 * <pre>{@code
 * @JsonbProperty("customName")
 * public String getExample() { .... }
 * }</pre>
 * is effectively the same as having it set like this using the builder
 * <pre>{@code
 * builder.name("customName", Scope.DESERIALIZATION);
 * }</pre>
 * </p>
 */
public interface PropertyCustomizationBuilder
        extends SerializerCustomizationBuilder<PropertyCustomizationBuilder, PropertyCustomization> {

    /**
     * Set custom property name for serialization and deserialization.
     *
     * @param name custom property name
     * @return updated builder instance
     */
    default PropertyCustomizationBuilder name(String name) {
        return name(name, Scope.BOTH);
    }

    /**
     * Set custom property name bound to the selected {@link Scope}.
     *
     * @param name custom property name
     * @param scope scope to which this name is bound
     * @return updated builder instance
     */
    PropertyCustomizationBuilder name(String name, Scope scope);

    /**
     * Whether this component can be nillable.
     *
     * This number format is used for serialization and deserialization of the property.
     *
     * @param nillable nillable component
     * @return updated builder instance
     */
    @Override
    default PropertyCustomizationBuilder nillable(boolean nillable) {
        return nillable(nillable, Scope.BOTH);
    }

    /**
     * Whether this component should be nillable in the given {@link Scope}.
     *
     * @param nillable nillable component
     * @param scope scope of the nillable
     * @return updated builder instance
     */
    PropertyCustomizationBuilder nillable(boolean nillable, Scope scope);

    /**
     * Set number format which should be used.
     *
     * This number format is used for serialization and deserialization of the property.
     *
     * @param numberFormat number format
     * @return updated builder instance
     */
    @Override
    default PropertyCustomizationBuilder numberFormat(String numberFormat) {
        return numberFormat(numberFormat, Scope.BOTH);
    }

    /**
     * Set number format locale which should be used.
     *
     * This locale is used for serialization and deserialization of the property.
     *
     * @param locale locale
     * @return updated builder instance
     */
    @Override
    default PropertyCustomizationBuilder numberFormat(Locale locale) {
        return numberFormat(locale, Scope.BOTH);
    }

    /**
     * Set number format and locale which should be used.
     *
     * Both number format and locale are used for serialization and deserialization of the property.
     *
     * @param numberFormat number format
     * @param locale locale
     * @return updated builder instance
     */
    @Override
    default PropertyCustomizationBuilder numberFormat(String numberFormat, Locale locale) {
        return numberFormat(numberFormat, locale, Scope.BOTH);
    }

    /**
     * Set {@link NumberFormat} instance which should be used.
     *
     * This {@link NumberFormat} instance is used for serialization and deserialization of the property.
     *
     * @param numberFormat pre created NumberFormat instance
     * @return updated builder instance
     */
    @Override
    default PropertyCustomizationBuilder numberFormat(NumberFormat numberFormat) {
        return numberFormat(numberFormat, Scope.BOTH);
    }

    /**
     * Set number format and locale which should be used in the given {@link Scope}.
     *
     * @param numberFormat number format
     * @param locale number format locale
     * @param scope scope of the format
     * @return updated builder instance
     */
    PropertyCustomizationBuilder numberFormat(String numberFormat, Locale locale, Scope scope);

    /**
     * Set {@link NumberFormat} instance which should be used in the given {@link Scope}.
     *
     * @param numberFormat pre created NumberFormat instance
     * @param scope scope of the format
     * @return updated builder instance
     */
    PropertyCustomizationBuilder numberFormat(NumberFormat numberFormat, Scope scope);

    /**
     * Set number format which should be used in the given {@link Scope}.
     *
     * @param numberFormat number format
     * @param scope scope of the format
     * @return updated builder instance
     */
    default PropertyCustomizationBuilder numberFormat(String numberFormat, Scope scope) {
        return numberFormat(numberFormat, Locale.getDefault(), scope);
    }

    /**
     * Set number format locale which should be used in the given {@link Scope}.
     *
     * @param locale number format locale
     * @param scope scope of the format
     * @return updated builder instance
     */
    default PropertyCustomizationBuilder numberFormat(Locale locale, Scope scope) {
        return numberFormat("", locale, scope);
    }

    /**
     * Set date format and locale which should be used.
     *
     * Both date format and locale are used for serialization and deserialization of the property.
     *
     * @param dateFormat date format
     * @param locale locale
     * @return updated builder instance
     */
    @Override
    default PropertyCustomizationBuilder dateFormat(String dateFormat, Locale locale) {
        return dateFormat(dateFormat, locale, Scope.BOTH);
    }

    /**
     * Set date format which should be used.
     *
     * This date format is used for serialization and deserialization of the property.
     *
     * @param dateFormat date format
     * @return updated builder instance
     */
    @Override
    default PropertyCustomizationBuilder dateFormat(String dateFormat) {
        return dateFormat(dateFormat, Locale.getDefault(), Scope.BOTH);
    }


    /**
     * Set date format locale which should be used.
     *
     * This date format locale is used for serialization and deserialization of the property.
     *
     * @param locale locale
     * @return updated builder instance
     */
    @Override
    default PropertyCustomizationBuilder dateFormat(Locale locale) {
        return dateFormat(JsonbDateFormat.DEFAULT_FORMAT, locale, Scope.BOTH);
    }

    /**
     * Set {@link DateFormat} instance which should be used.
     *
     * This {@link DateFormat} instance is used for serialization and deserialization of the property.
     *
     * @param dateFormat pre created DateFormat instance
     * @return updated builder instance
     */
    @Override
    default PropertyCustomizationBuilder dateFormat(DateFormat dateFormat) {
        return dateFormat(dateFormat, Scope.BOTH);
    }

    /**
     * Set date format and locale which should be used in the given {@link Scope}.
     *
     * @param dateFormat date format
     * @param locale date format locale
     * @param scope scope of the format
     * @return updated builder instance
     */
    PropertyCustomizationBuilder dateFormat(String dateFormat, Locale locale, Scope scope);

    /**
     * Set {@link DateFormat} instance which should be used in the given {@link Scope}.
     *
     * @param dateFormat pre created DateFormat instance
     * @return updated builder instance
     */
    PropertyCustomizationBuilder dateFormat(DateFormat dateFormat, Scope scope);

    /**
     * Set date format which should be used in the given {@link Scope}.
     *
     * @param dateFormat date format
     * @param scope scope of the format
     * @return updated builder instance
     */
    default PropertyCustomizationBuilder dateFormat(String dateFormat, Scope scope) {
        return dateFormat(dateFormat, Locale.getDefault(), scope);
    }

    /**
     * Set date format locale which should be used in the given {@link Scope}.
     *
     * @param locale date format locale
     * @param scope scope of the format
     * @return updated builder instance
     */
    default PropertyCustomizationBuilder dateFormat(Locale locale, Scope scope) {
        return dateFormat(JsonbDateFormat.DEFAULT_FORMAT, locale, scope);
    }

    /**
     * Whether the property is transient.
     *
     * @param isTransient transient property
     * @return updated builder instance
     */
    PropertyCustomizationBuilder transientProperty(boolean isTransient, Scope scope);

}
