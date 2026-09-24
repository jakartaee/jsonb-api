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

package jakarta.json.bind.config;

/**
 * <p>Specifies a predefined property name conversion for serialization and deserialization.</p>
 *
 * <p>This interface serves two purposes:</p>
 * <ul>
 *   <li>It defines the globally predefined naming strategy constants (e.g. {@link #IDENTITY},
 *       {@link #LOWER_CASE_WITH_DASHES}) that can be passed to
 *       {@link jakarta.json.bind.JsonbConfig#withPropertyNamingStrategy(String)}.</li>
 *   <li>It can be implemented directly to provide a custom naming strategy, which can be passed to
 *       {@link jakarta.json.bind.JsonbConfig#withPropertyNamingStrategy(PropertyNamingStrategy)}.</li>
 * </ul>
 *
 * <p>All naming stategies apply to the <em>identity property name</em>
 *    which is determined by one of the following program elements: </p>
 * <ul>
 *   <li>field name</li>
 *   <li>JavaBean property name</li>
 *   <li>record component name</li>
 *   <li>record virtual attribute name</li>
 * </ul>
 *
 * <p>A naming strategy applies globally to all serialized and deserialized types.
 * The stategy transforms the <em>identity property name</em> into a JSON property name.</p>
 *
 * <p><b>Interaction with other naming customizations</b></p>
 * <p>A {@link jakarta.json.bind.annotation.JsonbProperty} annotation on a field,
 * record component, accessor method, virtual attribute, or constructor/factory-method parameter takes
 * precedence over this strategy for that individual property. The strategy is applied to properties
 * only when no such annotation is present.</p>
 *
 * @see jakarta.json.bind.JsonbConfig
 * @see jakarta.json.bind.annotation.JsonbProperty
 * @since JSON Binding 1.0
 */
public interface PropertyNamingStrategy {

    /**
     * <p>The <em>identity property name</em> is used unchanged as the JSON property name.</p>
     *
     * <p>This is the default naming stategy.</p>
     */
    String IDENTITY = "IDENTITY";

    /**
     * <p>The <em>identity property name</em> is transformed to lower case with dashes.</p>
     *
     * <p>Dashes are inserted at camel-case boundaries in the identity property name</p>
     *
     * <p>For example, {@code myPropertyName} becomes
     * {@code my-property-name}.</p>
     */
    String LOWER_CASE_WITH_DASHES = "LOWER_CASE_WITH_DASHES";

    /**
     * <p>The <em>identity property name</em> is transformed to lower case with underscores.</p>
     *
     * <p>Underscores are inserted at camel-case boundaries in the identity
     * property name.</p>
     *
     * <p>For example, {@code myPropertyName} becomes
     * {@code my_property_name}.</p>
     */
    String LOWER_CASE_WITH_UNDERSCORES = "LOWER_CASE_WITH_UNDERSCORES";

    /**
     * <p>The first character of the <em>identity property name</em> is capitalized;
     * the remainder is unchanged.</p>
     *
     * <p>For example, {@code myPropertyName} becomes
     * {@code MyPropertyName}.</p>
     */
    String UPPER_CAMEL_CASE = "UPPER_CAMEL_CASE";

    /**
     * <p>The first character of the property name is capitalized and camel-case
     * word boundaries are replaced with spaces.</p>
     *
     * <p>For example, {@code myPropertyName} becomes
     * {@code My Property Name}.</p>
     */
    String UPPER_CAMEL_CASE_WITH_SPACES = "UPPER_CAMEL_CASE_WITH_SPACES";

    /**
     * <p>During serialization, the {@link #IDENTITY} strategy is used;
     * the property name is written to JSON unchanged.</p>
     *
     * <p>During deserialization, JSON property name matching is case-insensitive.</p>
     *
     * <p>For example, a JSON property named
     * {@code PropertyNAME} will be mapped to the <em>identity property name</em>
     * {@code propertyName}.</p>
     */
    String CASE_INSENSITIVE = "CASE_INSENSITIVE";

    /**
     * <p>Translates an <em>identity property name</em> into its JSON property name
     * representation according to this strategy.</p>
     *
     * <p>The returned value is used as the JSON property name during serialization
     * and as the expected JSON property name during deserialization,
     * unless overridden by a {@link jakarta.json.bind.annotation.JsonbProperty} annotation.</p>
     *
     * @param propertyName <em>identity property name</em> to translate.
     * @return the translated JSON property name.
     */
    String translateName(String propertyName);
}
