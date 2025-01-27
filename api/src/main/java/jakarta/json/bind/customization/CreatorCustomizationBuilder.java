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

import java.util.function.Consumer;

/**
 * Builder of the {@link CreatorCustomization} instance.
 *
 * When no creator method name is specified, it is assumed that creator is constructor.
 */
public interface CreatorCustomizationBuilder {

    /**
     * Add {@link ParamCustomization} instance.
     *
     * All creator parameters are required to be added in exact order as they are on decorated factory method/constructor.
     *
     * @param creatorParam creator parameter
     * @return updated builder instance
     */
    CreatorCustomizationBuilder addParam(ParamCustomization creatorParam);

    /**
     * Add new {@link ParamCustomization} of the property.
     * <br>
     * Shortcut method to the {@link #addParam(ParamCustomization)}. It is not required to create {@link CreatorCustomizationBuilder}
     * since this method will create {@link ParamCustomization} based on the provided parameter class and json name and by calling
     * {@link ParamCustomization#create(Class, String)} method. No further customizations will be applied.
     * <br>
     * All creator parameters are required to be added in exact order as they are on decorated factory method/constructor.
     *
     * @param parameterClass class of the parameter
     * @param jsonName json name of the parameter
     * @return updated builder instance
     */
    default CreatorCustomizationBuilder addParam(Class<?> parameterClass, String jsonName) {
        return addParam(ParamCustomization.create(parameterClass, jsonName));
    }

    /**
     * Add new {@link PropertyCustomization} of the property.
     * <br>
     * Shortcut method to the {@link #addParam(ParamCustomization)}. It is not required to create {@link CreatorCustomizationBuilder}
     * since this method will create {@link CreatorCustomizationBuilder} based on the provided parameter class and json name.
     * Created builder is provided over the paramBuilder.
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
    default CreatorCustomizationBuilder addParam(Class<?> parameterClass,
                                                 String jsonName,
                                                 Consumer<ParamCustomizationBuilder> paramBuilder) {
        ParamCustomizationBuilder builder = ParamCustomization.builder(parameterClass, jsonName);
        paramBuilder.accept(builder);
        return addParam(builder.build());
    }

    /**
     * Build the new instance of the {@link CreatorCustomization}.
     *
     * @return new {@link CreatorCustomization} instance
     */
    CreatorCustomization build();

}
