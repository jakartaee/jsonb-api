/*
 * Copyright (c) 2015, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * <p>Customizes the order in which properties are serialized for the annotated type.</p>
 *
 * <p>By default, properties are serialized in lexicographical order. This annotation
 * overrides that default and any order specified by
 * {@link jakarta.json.bind.config.PropertyOrderStrategy} for the annotated type.</p>
 *
 * <p><b>Usage</b></p>
 * <p>The {@code @JsonbPropertyOrder} annotation can be used with the following program elements:</p>
 * <ul>
 *   <li>class: ordering applies to all serializable fields and JavaBean properties.</li>
 *   <li>record: ordering applies to all serializable record components and record virtual attributes.</li>
 * </ul>
 *
 * <p><b>Partial ordering</b></p>
 * <p>A partial ordering may be specified by listing only a subset of properties in
 * {@link #value()}. Properties listed in the annotation are serialized first, in
 * the declared order. Any remaining properties not listed are serialized afterward
 * in an unspecified order.</p>
 *
 * <p><b>Property name resolution</b></p>
 * <p>Names provided in {@link #value()} must correspond to the original property
 * names as defined in the class or record, before any renaming customization
 * (such as {@link JsonbProperty} or a
 * {@link jakarta.json.bind.config.PropertyNamingStrategy}) is applied.</p>
 *
 * @since JSON Binding 1.0
 */
@JsonbAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
public @interface JsonbPropertyOrder {

    /**
     * The order in which properties are serialized. Names must correspond to the
     * original property or record component names as defined in the type, before
     * any renaming customization is applied.
     *
     * @return Array of property names defining the serialization order.
     */
    String[] value();
}
