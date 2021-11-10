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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configuration annotation of the polymorphic type handling.
 * <br>
 * This annotation is required to be present on the most top of all the classes,
 * polymorphism should be applied to.
 * <pre>{@code
 * // Example
 * @JsonbPolymorphicType(keyName = "@key")
 * interface Animal {}
 *
 * class Dog implements Animal {}
 * class Cat implements Animal {}
 * }</pre>
 * This annotation is closely tight with {@link JsonbSubtype}. It is recommended to use
 * {@link JsonbSubtype} annotations to specify all the possible classes and their aliases.
 * <br>
 */
@JsonbAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface JsonbPolymorphicType {

    /**
     * Key used for keeping polymorphic information when {@link Format#PROPERTY} is chosen.
     * Default value is {@code @type}.
     *
     * @return key name
     */
    String key() default "";

    /**
     * Specification of how the polymorphic information will be stored in the resulting JSON.
     *
     * @return polymorphic information storage type
     */
    Format format() default Format.PROPERTY;

    /**
     * Whether exact class names should be processed if no alias is specified for processed class.
     * <br>
     * Default value is false.
     *
     * @return classes without alias should be processed
     */
    boolean classNames() default false;

    /**
     * Allowed package names. This option is ignored if {@link JsonbPolymorphicType#classNames()}
     * option is set to false.
     * <br>
     * Only classes contained in the selected packages will be serialized/deserialized.
     * Classes with specified alias are not validated.
     * <br>
     * It is strongly recommended having allowed packages set up if classes without alias should be processed.
     * <br>
     * When no package is specified, all classes without alias are allowed.
     *
     * @return list of allowed packages
     */
    String[] allowedPackages() default {};

    /**
     * Allowed aliases of the given polymorphic type.
     *
     * @return list of allowed aliases
     */
    JsonbSubtype[] value() default {};

    /**
     * Format under which serialized polymorphic type should be handled.
     * <br>
     * Default polymorphic format is {@link Format#PROPERTY}.
     */
    enum Format {

        /**
         * Serialized object will be wrapped with another object.
         * This wrapping object will contain only one property with the name of the
         * polymorphic information (alias/class name) and a value assigned to this property is an actual
         * serialized object.
         * <br>
         * This format ignores specified {@link JsonbPolymorphicType#key()}.
         * <pre>{@code
         * // Example
         * @JsonbPolymorphicType(format = Format.WRAPPING_OBJECT, value = {
         *     @JsonbSubtype(alias = "dog", type = Dog.class)
         * })
         * interface Animal {}
         *
         * class Dog implements Animal {
         *     public String isDog = true;
         * }
         *
         * //Resulting json after dog instance serialization
         * {"dog":{"isDog":true}}
         * }</pre>
         */
        WRAPPING_OBJECT,

        /**
         * Serialized object will have one more property added to the resulting json.
         * This property will contain polymorphic information (alias/class name).
         * <br>
         * It is required to have nonempty {@link JsonbPolymorphicType#key()} specified when
         * using this format.
         * <pre>{@code
         * // Example
         * @JsonbPolymorphicType({
         *     @JsonbSubtype(alias = "dog", type = Dog.class)
         * })
         * interface Animal {}
         *
         * class Dog implements Animal {
         *     public String isDog = true;
         * }
         *
         * //Resulting json after dog instance serialization
         * {"@type":"dog","isDog":true}
         * }</pre>
         * This is the default format of the polymorphic handling and does not need to be
         * explicitly specified.
         */
        PROPERTY

    }

}
