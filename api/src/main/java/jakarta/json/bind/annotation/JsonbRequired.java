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

package jakarta.json.bind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Specifies which parameters of the {@link JsonbCreator} are required.
 * <br>
 * There are two possible values for this parameter: false or true.
 * If set to false, parameters are marked as optional and will not be required in the JSON document.
 * If set to true, however, parameters will be required (default behavior).
 * <br>
 * Annotation can be specified on method or constructor annotated also by {@link JsonbCreator} and affects
 * all the creator parameters. It is also possible to use this annotation on the creator parameter.
 *
 * @since JSON Binding 2.1
 */
@JsonbAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ANNOTATION_TYPE, METHOD, PARAMETER})
public @interface JsonbRequired {

    /**
     * Indicate if the parameter is required in the JSON document.
     *
     * @return true if parameter should be required, false if it is optional
     */
    boolean value() default true;

}
