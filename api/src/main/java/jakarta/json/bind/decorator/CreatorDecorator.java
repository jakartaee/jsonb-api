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

import java.util.List;
import java.util.Optional;

/**
 * Decorated creator customization.
 */
public interface CreatorDecorator {

    /**
     * Create new {@link CreatorDecoratorBuilder} instance.
     *
     * Builder created via this method targets constructors of the class it is bound to.
     *
     * @return new creator decorator builder instance
     */
    static CreatorDecoratorBuilder builder() {
        return BuilderProviderLoader.provider().newCreatorDecoratorBuilder();
    }

    /**
     * Create new {@link CreatorDecoratorBuilder} instance based on creator method name.
     *
     * @return new creator decorator builder instance
     */
    static CreatorDecoratorBuilder builder(String methodName) {
        return BuilderProviderLoader.provider().newCreatorDecoratorBuilder(methodName);
    }

    /**
     * Return creator method name if has been specified.
     * If the name is empty, it is handled as if it is null.
     *
     * @return specified creator method name, otherwise empty
     */
    Optional<String> factoryMethodName();

    /**
     * Return {@link List} of the registered parameters. The order of the parameters needs to be the same as they were added.
     * If no parameters were added, empty {@link List} is returned.
     *
     * @return creator parameters
     */
    List<ParamDecorator> params();

}
