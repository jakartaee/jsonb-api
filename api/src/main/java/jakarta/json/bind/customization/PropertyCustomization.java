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

import java.util.Arrays;
import java.util.Optional;

import jakarta.json.bind.spi.JsonbProvider;

/**
 * Decorated property customization.
 */
public interface PropertyCustomization extends ScopedCustomization {

    /**
     * Create new {@link PropertyCustomizationBuilder} instance base on property name.
     * Property name is required to be non-null or empty.
     *
     * @param propertyName name of the decorated property
     * @return new property customization builder instance
     */
    static PropertyCustomizationBuilder builder(String propertyName) {
        if (propertyName == null || propertyName.isBlank()) {
            throw new IllegalStateException("Property name cannot be null or empty");
        }
        return JsonbProvider.provider().newPropertyCustomizationBuilder(propertyName);
    }

    /**
     * Return the original property name.
     *
     * @return property name
     */
    String getPropertyName();

    /**
     * Return if the property is transient in the given {@link Scope}.
     *
     * @param scope required scope
     * @return property transient state, otherwise empty
     */
    Optional<Boolean> getTransientProperty(Scope scope);

    /**
     * Return custom property name in the given {@link Scope}.
     *
     * @param scope required scope
     * @return property custom name, otherwise empty
     */
    Optional<String> getName(Scope scope);

    default PropertyCustomizationBuilder toBuilder() {
        PropertyCustomizationBuilder builder = builder(getPropertyName());
        getAdapter().ifPresent(builder::adapter);
        getDeserializer().ifPresent(builder::deserializer);
        getSerializer().ifPresent(builder::serializer);
        Arrays.stream(Scope.values())
                        .forEach(scope -> {
                            getTransientProperty(scope).ifPresent(value -> builder.transientProperty(value, scope));
                            getDateFormat(scope).ifPresent(value -> builder.dateFormat(value, scope));
                            getName(scope).ifPresent(value -> builder.name(value, scope));
                            getNillable(scope).ifPresent(value -> builder.nillable(value, scope));
                            getNumberFormat(scope).ifPresent(value -> builder.numberFormat(value, scope));
                        });
        return builder;
    }

}
