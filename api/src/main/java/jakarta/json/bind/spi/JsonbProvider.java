/*
 * Copyright (c) 2016, 2021 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.json.bind.spi;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.decorator.CreatorDecoratorBuilder;
import jakarta.json.bind.decorator.ParamDecoratorBuilder;
import jakarta.json.bind.decorator.PropertyDecoratorBuilder;
import jakarta.json.bind.decorator.TypeDecoratorBuilder;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Service provider for JSON Binding implementations.
 *
 * Provider implementors must implement all abstract methods.
 *
 * API clients can obtain instance of default provider by calling:
 * <pre>
 * {@code
 * JsonbProvider provider = JsonbProvider.provider();
 * }}</pre>
 *
 * Specific provider instance lookup:
 * <pre>
 * {@code
 * JsonbProvider provider;
 * try {
 *   JsonbProvider.provider("foo.bar.ProviderImpl");
 * } catch (JsonbException e) {
 *   // provider not found or could not be instantiated
 * }}</pre>
 * where '{@code foo.bar.ProviderImpl}' is a vendor implementation class extending
 * {@link jakarta.json.bind.spi.JsonbProvider} and identified to service loader as
 * specified in {@link java.util.ServiceLoader} documentation.
 * <br>
 * All the methods in this class are allowed to be called by multiple concurrent
 * threads.
 *
 * @see jakarta.json.bind.Jsonb
 * @see java.util.ServiceLoader
 * @since JSON Binding 1.0
 */
public abstract class JsonbProvider {

    /**
     * A constant representing the name of the default
     * {@link jakarta.json.bind.spi.JsonbProvider JsonbProvider} implementation class.
     */
    private static final String DEFAULT_PROVIDER = "org.eclipse.yasson.JsonBindingProvider";

    private static JsonbProvider instance = null;
    /**
     * Protected constructor.
     */
    protected JsonbProvider() {
    }

    /**
     * Creates a JSON Binding provider object by using the
     * {@link java.util.ServiceLoader#load(Class)} method. The first provider of
     * {@code JsonbProvider} class from list of providers returned by
     * {@code ServiceLoader.load} call is returned. If there are no available
     * service providers, this method tries to load the default service provider using
     * {@link Class#forName(String)} method.
     *
     * @see java.util.ServiceLoader
     *
     * @throws JsonbException if there is no provider found, or there is a problem
     *         instantiating the provider instance.
     *
     * @return {@code JsonbProvider} instance
     */
    @SuppressWarnings("UseSpecificCatch")
    public static JsonbProvider provider() {
        if (instance == null) {
            synchronized (JsonbProvider.class) {
                if (instance == null) {
                    ServiceLoader<JsonbProvider> loader = ServiceLoader.load(JsonbProvider.class);
                    Iterator<JsonbProvider> it = loader.iterator();
                    if (it.hasNext()) {
                        instance = it.next();
                    } else {
                        try {
                            Class<?> clazz = Class.forName(DEFAULT_PROVIDER);
                            instance = (JsonbProvider) clazz.newInstance();
                        } catch (ClassNotFoundException x) {
                            throw new JsonbException("JSON Binding provider " + DEFAULT_PROVIDER + " not found", x);
                        } catch (Exception x) {
                            throw new JsonbException("JSON Binding provider " + DEFAULT_PROVIDER
                                                             + " could not be instantiated: " + x, x);
                        }
                    }
                }
            }
        }
        return instance;
    }

    /**
     * Creates a JSON Binding provider object by using the
     * {@link java.util.ServiceLoader#load(Class)} method, matching {@code providerName}.
     * The first provider of {@code JsonbProvider} class from list of providers returned by
     * {@code ServiceLoader.load} call, matching providerName is returned.
     * If no such provider is found, JsonbException is thrown.
     *
     * @param providerName
     *      Class name ({@code class.getName()}) to be chosen from the list of providers
     *      returned by {@code ServiceLoader.load(JsonbProvider.class)} call.
     *
     * @throws JsonbException if there is no provider found, or there is a problem
     *         instantiating the provider instance.
     *
     * @throws NullPointerException if providerName is {@code null}.
     *
     * @see java.util.ServiceLoader
     *
     * @return {@code JsonbProvider} instance
     */
    @SuppressWarnings("UseSpecificCatch")
    public static JsonbProvider provider(final String providerName) {
        if (providerName == null) {
            throw new IllegalArgumentException();
        } else if (instance != null && providerName.equals(instance.getClass().getName())) {
            return instance;
        }
        ServiceLoader<JsonbProvider> loader = ServiceLoader.load(JsonbProvider.class);
        for (JsonbProvider provider : loader) {
            if (providerName.equals(provider.getClass().getName())) {
                return provider;
            }
        }

        throw new JsonbException("JSON Binding provider " + providerName + " not found",
                                 new ClassNotFoundException(providerName));
    }

    /**
     * Returns a new instance of {@link jakarta.json.bind.JsonbBuilder JsonbBuilder} class.
     *
     * {@link jakarta.json.bind.JsonbBuilder JsonbBuilder} provides necessary getter
     * methods to access required parameters.
     *
     * @return JsonbBuilder
     *      A new instance of class implementing {@link jakarta.json.bind.JsonbBuilder}.
     *      Always a non-null valid object.
     *
     * @see jakarta.json.bind.Jsonb
     * @see jakarta.json.bind.JsonbBuilder
     *
     * @throws JsonbException
     *      If an error was encountered while creating the {@link JsonbBuilder} instance.
     */
    public abstract JsonbBuilder create();

    /**
     * Return new {@link TypeDecoratorBuilder} instance based on decorated type class.
     *
     * @param decoratedType decorated type class
     * @return new builder instance
     */
    public abstract TypeDecoratorBuilder newTypeDecoratorBuilder(Class<?> decoratedType);

    /**
     * Return new {@link PropertyDecoratorBuilder} instance based on decorated property name.
     *
     * @param propertyName decorated property name
     * @return new builder instance
     */
    public abstract PropertyDecoratorBuilder newPropertyDecoratorBuilder(String propertyName);

    /**
     * Return new {@link CreatorDecoratorBuilder}.
     *
     * @return new builder instance
     */
    public abstract CreatorDecoratorBuilder newCreatorDecoratorBuilder();

    /**
     * Return new {@link CreatorDecoratorBuilder} instance based on creator method name.
     *
     * @param methodName decorated creator method name
     * @return new builder instance
     */
    public abstract CreatorDecoratorBuilder newCreatorDecoratorBuilder(String methodName);

    /**
     * Return new {@link ParamDecoratorBuilder} instance based on parameter class and its name in JSON document.
     *
     * @param paramClass decorated parameter class
     * @param jsonName decorated parameter json name
     * @return new builder instance
     */
    public abstract ParamDecoratorBuilder newCreatorParamBuilder(Class<?> paramClass, String jsonName);

}
