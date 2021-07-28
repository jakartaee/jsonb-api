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

package jakarta.json.bind.decorator;

import java.util.Optional;

import jakarta.json.bind.spi.JsonbProvider;

/**
 * Decorated property customization.
 */
public interface PropertyDecorator extends SerializerDecorator {

    /**
     * Create new {@link PropertyDecoratorBuilder} instance base on property name.
     * Property name is required to be non-null or empty.
     *
     * @param propertyName name of the decorated property
     * @return new property decorator builder instance
     */
    static PropertyDecoratorBuilder builder(String propertyName) {
        if (propertyName == null || propertyName.isBlank()) {
            throw new IllegalStateException("Property name cannot be null or empty");
        }
        return JsonbProvider.provider().newPropertyDecoratorBuilder(propertyName);
    }

    /**
     * Return if the property is transient in the given {@link Scope}.
     *
     * @param scope required scope
     * @return property transient state, otherwise empty
     */
    Optional<Boolean> transientProperty(Scope scope);

    /**
     * Return custom property name in the given {@link Scope}.
     *
     * @param scope required scope
     * @return property custom name, otherwise empty
     */
    Optional<String> name(Scope scope);

}
