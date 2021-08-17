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

import java.util.List;
import java.util.Optional;

import jakarta.json.bind.spi.JsonbProvider;

/**
 * Decorated creator customization.
 */
public interface CreatorCustomization {

    /**
     * Create new {@link CreatorCustomizationBuilder} instance.
     *
     * Builder created via this method targets constructors of the class it is bound to.
     *
     * @return new creator customization builder instance
     */
    static CreatorCustomizationBuilder builder() {
        return JsonbProvider.provider().newCreatorCustomizationBuilder();
    }

    /**
     * Create new {@link CreatorCustomizationBuilder} instance based on creator method name.
     *
     * @return new creator customization builder instance
     */
    static CreatorCustomizationBuilder builder(String methodName) {
        return JsonbProvider.provider().newCreatorCustomizationBuilder(methodName);
    }

    /**
     * Return creator method name if has been specified.
     * If the name is empty, it is handled as if it is null.
     *
     * @return specified creator method name, otherwise empty
     */
    Optional<String> factoryMethodName();

    /**
     * Return immutable {@link List} of the registered parameters. The order of the parameters needs to be the same as they were added.
     * If no parameters were added, empty {@link List} is returned.
     *
     * @return creator parameters
     */
    List<ParamCustomization> params();

}
