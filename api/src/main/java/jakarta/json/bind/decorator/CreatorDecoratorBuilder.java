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

import java.util.function.Consumer;

/**
 * Builder of the {@link CreatorDecorator} instance.
 *
 * When no creator method name is specified, it is assumed that creator is constructor.
 */
public interface CreatorDecoratorBuilder {

    /**
     * Add {@link ParamDecorator} instance.
     *
     * All creator parameters are required to be added in exact order as they are on decorated factory method/constructor.
     *
     * @param creatorParam creator parameter
     * @return updated builder instance
     */
    CreatorDecoratorBuilder addParameter(ParamDecorator creatorParam);

    /**
     * Add new {@link ParamDecorator} of the property.
     * <br>
     * Shortcut method to the {@link #addParameter(ParamDecorator)}. It is not required to create {@link CreatorDecoratorBuilder}
     * since this method will create {@link ParamDecorator} based on the provided parameter class and json name and by calling
     * {@link ParamDecorator#create(Class, String)} method. No further customizations will be applied.
     * <br>
     * All creator parameters are required to be added in exact order as they are on decorated factory method/constructor.
     *
     * @param parameterClass class of the parameter
     * @param jsonName json name of the parameter
     * @return updated builder instance
     */
    default CreatorDecoratorBuilder addParameter(Class<?> parameterClass, String jsonName) {
        return addParameter(ParamDecorator.create(parameterClass, jsonName));
    }

    /**
     * Add new {@link PropertyDecorator} of the property.
     * <br>
     * Shortcut method to the {@link #addParameter(ParamDecorator)}. It is not required to create {@link CreatorDecoratorBuilder}
     * since this method will create is based on the provided parameter class and json name. Created builder is provided over
     * the paramBuilder.
     * <br>
     * All creator parameters are required to be added in exact order as they are on decorated factory method/constructor.
     * <br>
     * Example usage:
     * <pre>{@code
     * creatorBuilder.addParameter(String.class, "jsonName", paramBuilder -> paramBuilder.nillable(true));
     * }</pre>
     *
     * @param parameterClass class of the parameter
     * @param jsonName json name of the parameter
     * @param paramBuilder builder used to customize parameter
     * @return updated builder instance
     */
    default CreatorDecoratorBuilder addParameter(Class<?> parameterClass,
                                                 String jsonName,
                                                 Consumer<ParamDecoratorBuilder> paramBuilder) {
        ParamDecoratorBuilder builder = ParamDecorator.builder(parameterClass, jsonName);
        paramBuilder.accept(builder);
        return addParameter(builder.build());
    }

    /**
     * Build the new instance of the {@link CreatorDecorator}.
     *
     * @return new {@link CreatorDecorator} instance
     */
    CreatorDecorator build();

}
