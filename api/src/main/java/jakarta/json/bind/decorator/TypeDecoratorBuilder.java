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

package jakarta.json.bind.decorator;

import java.util.Objects;
import java.util.function.Consumer;

import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.json.bind.config.PropertyVisibilityStrategy;

/**
 * Builder of the specific type customization.
 */
public interface TypeDecoratorBuilder extends SerializerDecoratorBuilder<TypeDecoratorBuilder, TypeDecorator> {

    /**
     * Set {@link PropertyOrderStrategy} which should be used.
     *
     * @param strategy property order strategy
     * @return updated builder instance
     */
    TypeDecoratorBuilder propertyOrder(PropertyOrderStrategy strategy);

    /**
     * Set {@link PropertyVisibilityStrategy} which should be used.
     *
     * @param strategy property visibility strategy
     * @return updated builder instance
     */
    TypeDecoratorBuilder visibilityStrategy(PropertyVisibilityStrategy strategy);

    /**
     * Add new {@link PropertyDecorator} of the property.
     *
     * @param propertyDecorator property decorator
     * @return updated builder instance
     */
    TypeDecoratorBuilder property(PropertyDecorator propertyDecorator);

    /**
     * Add new {@link PropertyDecorator} of the property.
     * <br>
     * Shortcut method to the {@link #property(PropertyDecorator)}. It is not required to create {@link PropertyDecoratorBuilder}
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
    default TypeDecoratorBuilder property(String propertyName, Consumer<PropertyDecoratorBuilder> propertyBuilder) {
        PropertyDecoratorBuilder builder = PropertyDecorator.builder(Objects.requireNonNull(propertyName));
        propertyBuilder.accept(builder);
        return property(builder.build());
    }

    /**
     * Add new {@link CreatorDecorator} of the type.
     *
     * @param creatorDecorator creator decorator
     * @return updated builder instance
     */
    TypeDecoratorBuilder creator(CreatorDecorator creatorDecorator);

    /**
     * Add new {@link CreatorDecorator} of the type.
     * <br>
     * Shortcut method to the {@link #creator(CreatorDecorator)}. It is not required to create {@link CreatorDecoratorBuilder}
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
    default TypeDecoratorBuilder creator(String creatorMethodName, Consumer<CreatorDecoratorBuilder> creatorBuilder) {
        CreatorDecoratorBuilder builder = CreatorDecorator.builder(creatorMethodName);
        creatorBuilder.accept(builder);
        return creator(builder.build());
    }

    /**
     * Add new {@link CreatorDecorator} of the type.
     * <br>
     * Shortcut method to the {@link #creator(CreatorDecorator)}. It is not required to create {@link CreatorDecoratorBuilder}
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
    default TypeDecoratorBuilder creator(Consumer<CreatorDecoratorBuilder> creatorBuilder) {
        CreatorDecoratorBuilder builder = CreatorDecorator.builder();
        creatorBuilder.accept(builder);
        return creator(builder.build());
    }

}
