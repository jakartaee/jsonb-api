/*
 * Copyright (c) 2016, 2021 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.json.bind.annotation;


import jakarta.json.bind.serializer.JsonbDeserializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *     Annotation provides way how to set custom JsonbDeserializer to field or JavaBean property.
 * </p>
 *
 * <p><b>Usage</b></p>
 * <p> The {@code @JsonbDeserializer} annotation can be used with the following
 *     program elements:
 * <ul>
 *   <li> type </li>
 *   <li> field </li>
 *   <li> method </li>
 *   <li> creator parameter </li>
 * </ul>
 *
 * @since JSON Binding 1.0
 */
@JsonbAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface JsonbTypeDeserializer {

    /**
     * Custom {@link JsonbDeserializer} which provides custom mapping for given field or JavaBean property.
     *
     * @return Deserializer to use.
     */
    Class<? extends JsonbDeserializer> value();
}
