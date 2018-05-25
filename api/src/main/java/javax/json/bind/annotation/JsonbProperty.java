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

package javax.json.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Allows customization of field (or JavaBean property) name.This name is used either in serialization or
 * in deserialization.</p>
 *
 * <p><b>Usage</b></p>
 * <p>The {@code @JsonbProperty} annotation can be used with the following program elements:</p>
 * <ul>
 *   <li> a JavaBean property </li>
 *   <li> field </li>
 *   <li> parameter </li>
 * </ul>
 *
 * @since JSON Binding 1.0
 */
@JsonbAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface JsonbProperty {

    /**
     * Customized name of the field (or JavaBean property).
     *
     * @return Customized property name.
     */
    String value() default "";

    /**
     * Switches o/off serialization of null values.
     *
     * @return True if field with null value should be serialized as key/value pair into JSON with null value.
     */
    boolean nillable() default false;
}
