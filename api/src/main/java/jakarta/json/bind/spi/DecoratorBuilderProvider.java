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

package jakarta.json.bind.spi;

import jakarta.json.bind.decorator.CreatorDecoratorBuilder;
import jakarta.json.bind.decorator.ParamDecoratorBuilder;
import jakarta.json.bind.decorator.PropertyDecoratorBuilder;
import jakarta.json.bind.decorator.TypeDecoratorBuilder;

/**
 * Service provider for decorator builders.
 */
public interface DecoratorBuilderProvider {

    /**
     * Return new {@link TypeDecoratorBuilder} instance based on decorated type class.
     *
     * @param decoratedType decorated type class
     * @return new builder instance
     */
    TypeDecoratorBuilder newTypeDecoratorBuilder(Class<?> decoratedType);

    /**
     * Return new {@link PropertyDecoratorBuilder} instance based on decorated property name.
     *
     * @param propertyName decorated property name
     * @return new builder instance
     */
    PropertyDecoratorBuilder newPropertyDecoratorBuilder(String propertyName);

    /**
     * Return new {@link CreatorDecoratorBuilder}.
     *
     * @return new builder instance
     */
    CreatorDecoratorBuilder newCreatorDecoratorBuilder();

    /**
     * Return new {@link CreatorDecoratorBuilder} instance based on creator method name.
     *
     * @param methodName decorated creator method name
     * @return new builder instance
     */
    CreatorDecoratorBuilder newCreatorDecoratorBuilder(String methodName);

    /**
     * Return new {@link ParamDecoratorBuilder} instance based on parameter class and its name in JSON document.
     *
     * @param paramClass decorated parameter class
     * @param jsonName decorated parameter json name
     * @return new builder instance
     */
    ParamDecoratorBuilder newCreatorParamBuilder(Class<?> paramClass, String jsonName);

}
