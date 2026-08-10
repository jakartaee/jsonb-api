/*
 * Copyright (c) 2015, 2026 Oracle and/or its affiliates. All rights reserved.
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
 * <p>By default, deserialization of a class invokes its no-argument constructor, and deserialization
 * of a record invokes its canonical constructor. A record may also declare a compact constructor
 * whose body is merged into the canonical constructor at compile time; the two are indistinguishable
 * at runtime and both serve as the canonical deserialization path without requiring
 * {@code @JsonbCreator}.</p>
 *
 * <p>This annotation identifies a custom constructor or static factory method to invoke when
 * creating an instance of the associated class or record during deserialization.</p>
 *
 * <p>Only one constructor or static factory method per class or record may be annotated
 * with {@code @JsonbCreator}.</p>
 *
 * <p>Parameters of the annotated constructor or static factory method may themselves be
 * annotated, for example with {@link JsonbProperty}.</p>
 *
 * <p><b>Usage</b></p>
 * <p>The {@code @JsonbCreator} annotation can be used with the following program elements:</p>
 * <ul>
 *   <li> method </li>
 *   <li> constructor </li>
 * </ul>
 *
 * <p><b>Since 3.1:</b> {@code @JsonbCreator} may also be placed on a non-canonical constructor
 * or static factory method of a Java record to designate an alternative deserialization path.</p>
 *
 * @since JSON Binding 1.0
 */
@JsonbAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface JsonbCreator {
}
