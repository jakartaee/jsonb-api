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

package jakarta.json.bind.config;

/**
 * <p>Specifies predefined property order stategy for serialization.</p>
 *
 * <p>This strategy can be set globally via
 * {@link jakarta.json.bind.JsonbConfig#withPropertyOrderStrategy(String)}.</p>
 *
 * <p><b>Interaction with other ordering customizations</b></p>
 * <p>This strategy applies globally to all serialized types, but its effect can be
 * overridden at the type level. The following precedence rules apply:</p>
 * <ol>
 *   <li>{@link jakarta.json.bind.annotation.JsonbPropertyOrder} on a class or record
 *       takes precedence over this strategy for that specific type. Properties listed
 *       in that annotation are serialized first in the declared order; any remaining
 *       properties follow in an unspecified order.</li>
 *   <li>This strategy is applied to all other types that do not carry a
 *       {@link jakarta.json.bind.annotation.JsonbPropertyOrder} annotation.</li>
 * </ol>
 *
 * <p><b>Interaction with property renaming</b></p>
 * <p>When {@link LEXICOGRAPHICAL} or {@link REVERSE} is used, ordering is applied
 * to the final JSON property names; that is, names after any renaming customization
 * (such as {@link jakarta.json.bind.annotation.JsonbProperty} or a
 * {@link PropertyNamingStrategy}) has been applied.</p>
 *
 * @see jakarta.json.bind.JsonbConfig
 * @see jakarta.json.bind.annotation.JsonbPropertyOrder
 * @see jakarta.json.bind.annotation.JsonbProperty
 * @since JSON Binding 1.0
 */
public final class PropertyOrderStrategy {

    /**
     * Private constructor to disallow instantiation.
     */
    private PropertyOrderStrategy() { };

    /**
     * Properties are serialized in ascending lexicographical order of their
     * JSON property names. If a property name has been customized via
     * {@link jakarta.json.bind.annotation.JsonbProperty} or a
     * {@link PropertyNamingStrategy}, the customized name is used for ordering.
     */
    public static final String LEXICOGRAPHICAL = "LEXICOGRAPHICAL";

    /**
     * The order of properties is not specified and may vary across
     * serializations. No ordering guarantees are provided.
     */
    public static final String ANY = "ANY";

    /**
     * Properties are serialized in descending lexicographical order of their
     * JSON property names; the reverse of {@link #LEXICOGRAPHICAL}. If a
     * property name has been customized via
     * {@link jakarta.json.bind.annotation.JsonbProperty} or a
     * {@link PropertyNamingStrategy}, the customized name is used for ordering.
     */
    public static final String REVERSE = "REVERSE";
}
