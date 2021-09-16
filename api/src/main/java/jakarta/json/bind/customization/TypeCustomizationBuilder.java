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

package jakarta.json.bind.customization;

import java.util.Objects;
import java.util.function.Consumer;

import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.json.bind.config.PropertyVisibilityStrategy;

/**
 * Builder of the specific type customization.
 */
public interface TypeCustomizationBuilder extends SerializationCustomizationBuilder<TypeCustomizationBuilder, TypeCustomization> {

    /**
     * Set {@link PropertyOrderStrategy} which should be used.
     *
     * @param strategy property order strategy
     * @return updated builder instance
     */
    TypeCustomizationBuilder propertyOrderStrategy(PropertyOrderStrategy strategy);

    /**
     * Ignore property order strategy.
     *
     * @return updated builder instance
     */
    TypeCustomizationBuilder ignorePropertyOrderStrategy();

    /**
     * Set {@link PropertyVisibilityStrategy} which should be used.
     *
     * @param strategy property visibility strategy
     * @return updated builder instance
     */
    TypeCustomizationBuilder visibilityStrategy(PropertyVisibilityStrategy strategy);

    /**
     * Ignore visibility strategy.
     *
     * @return updated builder instance
     */
    TypeCustomizationBuilder ignoreVisibilityStrategy();

    /**
     * Add new {@link PropertyCustomization} of the property.
     *
     * @param propertyCustomization property customization
     * @return updated builder instance
     */
    TypeCustomizationBuilder property(PropertyCustomization propertyCustomization);

    /**
     * Add new {@link PropertyCustomization} of the property.
     * <br>
     * Shortcut method to the {@link #property(PropertyCustomization)}. It is not required to create {@link PropertyCustomizationBuilder}
     * since this method will create it based on the provided property name and expose it over the propertyBuilder parameter.
     * <br>
     * Example usage:
     * <pre>{@code
     * typeBuilder.property("exampleProperty", propertyBuilder -> propertyBuilder.nillable(true));
     * }</pre>
     * @param propertyName name of the class property
     * @param propertyBuilder builder used to customize property
     * @return updated builder instance
     */
    default TypeCustomizationBuilder property(String propertyName, Consumer<PropertyCustomizationBuilder> propertyBuilder) {
        PropertyCustomizationBuilder builder = PropertyCustomization.builder(Objects.requireNonNull(propertyName));
        propertyBuilder.accept(builder);
        return property(builder.build());
    }

    /**
     * Add new {@link CreatorCustomization} of the type.
     *
     * @param creatorCustomization creator customization
     * @return updated builder instance
     */
    TypeCustomizationBuilder creator(CreatorCustomization creatorCustomization);

    /**
     * Add new {@link CreatorCustomization} of the type.
     * <br>
     * Shortcut method to the {@link #creator(CreatorCustomization)}. It is not required to create {@link CreatorCustomizationBuilder}
     * since this method will create it based on the creator method name and expose it over the creatorBuilder parameter.
     * <br>
     * Example usage:
     * <pre>{@code
     * typeBuilder.creator("factoryMethodName",
     *                     creatorBuilder -> creatorBuilder.addParameter(String.class, "firstParameter"));
     * }</pre>
     * @param creatorMethodName creator method name
     * @param creatorBuilder creator builder instance consumer
     * @return updated builder instance
     */
    default TypeCustomizationBuilder creator(String creatorMethodName, Consumer<CreatorCustomizationBuilder> creatorBuilder) {
        CreatorCustomizationBuilder builder = CreatorCustomization.builder(creatorMethodName);
        creatorBuilder.accept(builder);
        return creator(builder.build());
    }

    /**
     * Add new {@link CreatorCustomization} of the type.
     * <br>
     * Shortcut method to the {@link #creator(CreatorCustomization)}. It is not required to create {@link CreatorCustomizationBuilder}
     * since this method will create it and expose it over the creatorBuilder parameter. Since no factory method name is
     * provided, this creator builder is targeting constructors.
     * <br>
     * Example usage:
     * <pre>{@code
     * typeBuilder.creator(creatorBuilder -> creatorBuilder.addParameter(String.class, "firstParameter"));
     * }</pre>
     * @param creatorBuilder creator builder instance consumer
     * @return updated builder instance
     */
    default TypeCustomizationBuilder creator(Consumer<CreatorCustomizationBuilder> creatorBuilder) {
        CreatorCustomizationBuilder builder = CreatorCustomization.builder();
        creatorBuilder.accept(builder);
        return creator(builder.build());
    }

}
