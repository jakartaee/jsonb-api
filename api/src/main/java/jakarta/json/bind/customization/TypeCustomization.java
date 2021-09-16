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

import java.util.Map;
import java.util.Optional;

import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.json.bind.config.PropertyVisibilityStrategy;
import jakarta.json.bind.spi.JsonbProvider;

/**
 * Type customization.
 */
public interface TypeCustomization extends SerializationCustomization, ScopelessCustomization {

    /**
     * Create new {@link TypeCustomizationBuilder} instance base on type class.
     * Parameter typeClass is required to be non-null.
     *
     * @param typeClass type class
     * @return new type customization builder instance
     */
    static TypeCustomizationBuilder builder(Class<?> typeClass) {
        return JsonbProvider.provider().newTypeCustomizationBuilder(typeClass);
    }

    /**
     * Return type which is this customization bound to.
     *
     * @return type which is this customization bound to
     */
    Class<?> getType();

    /**
     * Return {@link PropertyOrderStrategy} of the decorated type.
     *
     * @return property order strategy instance, otherwise empty
     */
    Optional<PropertyOrderStrategy> getPropertyOrderStrategy();

    /**
     * Return {@link PropertyVisibilityStrategy} of the decorated type.
     *
     * @return property visibility strategy instance, otherwise empty
     */
    Optional<PropertyVisibilityStrategy> getPropertyVisibilityStrategy();

    /**
     * Return registered property customizations.
     * If no property is registered, empty {@link Map} is returned.
     *
     * @return map of the registered property customizations, otherwise empty map
     */
    Map<String, ? extends PropertyCustomization> getProperties();

    /**
     * Return {@link CreatorCustomization} of the decorated type.
     *
     * @return creator customization instance, otherwise empty
     */
    Optional<CreatorCustomization> getCreator();

    /**
     * Convert {@link TypeCustomization} to the {@link TypeCustomizationBuilder} with prefilled values from original object.
     *
     * @return new {@link TypeCustomizationBuilder} instance
     */
    default TypeCustomizationBuilder toBuilder() {
        TypeCustomizationBuilder builder = builder(getType());
        getNillable().ifPresent(builder::nillable);
        getDeserializer().ifPresent(builder::deserializer);
        getSerializer().ifPresent(builder::serializer);
        getAdapter().ifPresent(builder::adapter);
        getNumberFormat().ifPresent(builder::numberFormat);
        getDateFormat().ifPresent(builder::dateFormat);
        getCreator().ifPresent(builder::creator);
        getPropertyOrderStrategy().ifPresent(builder::propertyOrderStrategy);
        getPropertyVisibilityStrategy().ifPresent(builder::visibilityStrategy);
        getProperties().values().forEach(builder::property);
        return builder;
    }

}
