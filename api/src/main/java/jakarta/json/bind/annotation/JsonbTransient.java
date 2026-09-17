/*
 * Copyright (c) 2016, 2020 Oracle and/or its affiliates. All rights reserved.
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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;

/**
 * <p>Prevents serialization and/or deserialization of JSON properties.</p>
 *
 * <p><b>Usage</b></p>
 * <p>The {@code @JsonbTransient} annotation can be used with the following program elements:</p>
 * <ul>
 *   <li>field: prevents serialization and deserialization</li>
 *   <li>getter: prevents serialization</li>
 *   <li>setter: prevents deserialization</li>
 *   <li>record component: prevents serialization and deserialization</li>
 *   <li>record component accessor: prevents serialization</li>
 *   <li>record virtual attribute: prevents serialization</li>
 * </ul>
 *
 * <p>{@code @JsonbTransient} is mutually exclusive with all other JSON Binding defined annotations.
 * This behavior is enforced by throwing a {@link jakarta.json.bind.JsonbException} during serialization
 * and/or deserialization when the following combinations of annotations are present:</p>
 *
 * <table>
 *   <caption>Conditions under which a {@link jakarta.json.bind.JsonbException} is thrown</caption>
 *   <thead>
 *     <tr>
 *       <th>{@code @JsonbTransient} placement</th>
 *       <th>Additional annotation that triggers exception</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr>
 *       <td>field</td>
 *       <td>any other JSON Binding annotation on the same field, its getter, or its setter</td>
 *     </tr>
 *     <tr>
 *       <td>getter</td>
 *       <td>any other JSON Binding annotation on the corresponding field or the same getter (setter is unaffected)</td>
 *     </tr>
 *     <tr>
 *       <td>setter</td>
 *       <td>any other JSON Binding annotation on the corresponding field or the same setter (getter is unaffected)</td>
 *     </tr>
 *     <tr>
 *       <td>record component</td>
 *       <td>any other JSON Binding annotation on the same component or its accessor</td>
 *     </tr>
 *     <tr>
 *       <td>record component accessor</td>
 *       <td>any other JSON Binding annotation on the corresponding component or the same accessor</td>
 *     </tr>
 *     <tr>
 *       <td>record virtual attribute</td>
 *       <td>any other JSON Binding annotation on the same virtual attribute</td>
 *     </tr>
 *   </tbody>
 * </table>
 *
 * @since JSON Binding 1.0
 */
@JsonbAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ANNOTATION_TYPE, FIELD, METHOD, RECORD_COMPONENT})
public @interface JsonbTransient { }

