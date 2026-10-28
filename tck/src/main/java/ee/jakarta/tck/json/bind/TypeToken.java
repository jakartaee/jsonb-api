/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

package ee.jakarta.tck.json.bind;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Utility class to capture and represent generic {@link Type} information at runtime without type erasure.
 *
 * <p>Usage:
 * <pre>
 *     Type type = new TypeToken&lt;GenericContainer&lt;String&gt;&gt;() { }.getType();
 * </pre>
 *
 * @param <T> the type represented by this token
 */
public abstract class TypeToken<T> {

    private final Type type;

    /**
     * Constructs a new type token by extracting the actual type argument from the generic superclass.
     */
    protected TypeToken() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        } else {
            throw new IllegalArgumentException("TypeToken must be instantiated with generic type parameters");
        }
    }

    /**
     * Returns the {@link Type} captured by this token.
     *
     * @return the captured generic type
     */
    public Type getType() {
        return this.type;
    }
}
