/*
 * Copyright (c) 2015, 2021 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.json.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Customizes serialization and/or deserialziaton of JSON property names.</p>
 *
 * <p>By default, property names are serialized unchanged (identity transformation).
 * This annotation overrides that default for the annotated program element.</p>
 *
 * <p><b>Usage</b></p>
 * <p>The {@code @JsonbProperty} annotation can be used with the following program elements:</p>
 * <ul>
 *   <li>field: used for both serialization and deserialization.</li>
 *   <li>record component: used for both serialization and deserialization.</li>
 *   <li>getter: used only for serialization.</li>
 *   <li>setter: used only for deserialization.</li>
 *   <li>record component accessor method: used only for serialization.</li>
 *   <li>record virtual attribute method: used only for serialization.</li>
 *   <li>parameter: used on a {@link JsonbCreator} annotated constructor or
 *        static factory method parameter only for deserialization.</li>
 * </ul>
 *
 * <p>It is possible to specify different custom names on the getter and setter of the same
 * JavaBean property, in which case the getter name is used for serialization and the setter
 * name is used for deserialization independently.</p>
 *
 * @since JSON Binding 1.0
 */
@JsonbAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD,
         ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
public @interface JsonbProperty {

    /**
     * Customized JSON property name for the annotated element.
     *
     * @return Customized property name.
     */
    String value() default "";

    /**
     * Switches on/off serialization of null values.
     *
     * @return True if field with null value should be serialized as key/value pair into JSON with null value.
     * @deprecated Please use the {@link JsonbNillable} annotation instead.
     * This element will be removed in the future.
     */
    @Deprecated(since = "2.1")
    boolean nillable() default false;
}
