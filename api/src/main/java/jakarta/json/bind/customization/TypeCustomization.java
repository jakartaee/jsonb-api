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
 * Decorated type customization.
 */
public interface TypeCustomization extends SerializerCustomization, ScopelessCustomization {

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
     * Return {@link PropertyOrderStrategy} of the decorated type.
     *
     * @return property order strategy instance, otherwise empty
     */
    Optional<PropertyOrderStrategy> propertyOrderStrategy();

    /**
     * Return {@link PropertyVisibilityStrategy} of the decorated type.
     *
     * @return property visibility strategy instance, otherwise empty
     */
    Optional<PropertyVisibilityStrategy> propertyVisibilityStrategy();

    /**
     * Return registered property customizations.
     * If no property is registered, empty {@link Map} is returned.
     *
     * @return map of the registered property customizations, otherwise empty map
     */
    Map<String, PropertyCustomization> properties();

    /**
     * Return {@link CreatorCustomization} of the decorated type.
     *
     * @return creator customization instance, otherwise empty
     */
    Optional<CreatorCustomization> creator();

}
