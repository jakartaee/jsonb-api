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

import jakarta.json.bind.serializer.JsonbSerializer;

/**
 * Extension of the {@link JsonbCustomizationBuilder} over the serialization specific methods.
 */
public interface SerializerCustomizationBuilder<T extends SerializerCustomizationBuilder<T, B>, B extends JsonbCustomization>
        extends JsonbCustomizationBuilder<T, B> {

    /**
     * Set {@link JsonbSerializer} which should be used for this component.
     *
     * @param serializer component deserializer
     * @return updated builder instance
     */
    T serializer(JsonbSerializer<?> serializer);

}
