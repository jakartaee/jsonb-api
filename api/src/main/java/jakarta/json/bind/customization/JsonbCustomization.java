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

import java.util.Optional;

import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.json.bind.serializer.JsonbDeserializer;

/**
 * Common interface for all the customizations.
 */
public interface JsonbCustomization {

    /**
     * Return {@link JsonbDeserializer} of the component.
     *
     * @return component deserializer instance, otherwise empty
     */
    Optional<JsonbDeserializer<?>> getDeserializer();

    /**
     * Return {@link JsonbAdapter} of the component.
     *
     * @return component adapter instance, otherwise empty
     */
    Optional<JsonbAdapter<?, ?>> getAdapter();

}
