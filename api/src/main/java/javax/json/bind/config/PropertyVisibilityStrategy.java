/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package javax.json.bind.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>Provides mechanism how to define customized property visibility strategy.</p>
 *
 * <p>This strategy can be set via {@link javax.json.bind.JsonbConfig}.</p>
 *
 * @see javax.json.bind.JsonbConfig
 * @since JSON Binding 1.0
 */
public interface PropertyVisibilityStrategy {

    /**
     * Responds whether the given field should be considered
     * as the JsonbProperty.
     *
     * @param field member of the class
     * @return true if member should be visible
     */
    boolean isVisible(Field field);

    /**
     * Responds whether the given method should be considered
     * as the JsonbProperty.
     *
     * @param method member of the class
     * @return true if member should be visible
     */
    boolean isVisible(Method method);
}
