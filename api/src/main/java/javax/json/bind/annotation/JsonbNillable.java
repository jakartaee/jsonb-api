/*
 * Copyright (c) 2016, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;

/**
 * <p>Specifies how fields having null values are serialized into JSON.</p>
 *
 * <p>There are two possible values which can be specified. In case of true, fields are serialized as
 * key/value pair with value null. In case of false, fields will not be serialized (default behaviour).</p>
 *
 * <p>Annotation can be specified on type or on package and affects all underlying properties and classes.,</p>
 *
 * <p>For similar functionality on a property level see {@link JsonbProperty}.
 *
 * @since JSON Binding 1.0
 */
@JsonbAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ANNOTATION_TYPE, TYPE, PACKAGE})
public @interface JsonbNillable {

    /**
     * Switches on/off serialization of properties with null value.
     *
     * @return True if field with null value should be serialized as key/value pair into JSON with null value.
     */
    boolean value() default true;
}
