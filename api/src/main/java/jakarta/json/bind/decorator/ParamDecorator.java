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

import java.util.Objects;

/**
 * Decorated parameter customization.
 */
public interface ParamDecorator extends JsonbDecorator {

    /**
     * Create new {@link ParamDecoratorBuilder} instance base on parameter class and its json name.
     * Both parameter class and json name are required to be non-null or empty.
     *
     * @param paramClass parameter class
     * @param jsonName parameter json name
     * @return new param decorator builder instance
     */
    static ParamDecoratorBuilder builder(Class<?> paramClass, String jsonName) {
        Objects.requireNonNull(paramClass, "Parameter class cannot be null");
        if (jsonName == null || jsonName.isBlank()) {
            throw new IllegalStateException("Json name cannot be null or empty");
        }
        return BuilderProviderLoader.provider().newCreatorParamBuilder(paramClass, jsonName);
    }

    /**
     * Create new instance of {@link ParamDecorator} base on parameter class and its json name.
     * Both parameter class and json name are required to be non-null or empty.
     *
     * @param paramClass parameter class
     * @param jsonName parameter json name
     * @return new parameter decorator instance
     */
    static ParamDecorator create(Class<?> paramClass, String jsonName) {
        Objects.requireNonNull(paramClass, "Parameter class cannot be null");
        if (jsonName == null || jsonName.isBlank()) {
            throw new IllegalStateException("Json name cannot be null or empty");
        }
        return builder(paramClass, jsonName).build();
    }

    /**
     * Return parameter json name.
     *
     * @return parameter json name
     */
    String jsonName();

    /**
     * Return parameter class.
     *
     * @return parameter class
     */
    Class<?> parameterClass();

}
