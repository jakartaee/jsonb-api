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

package ee.jakarta.tck.json.bind.customizedmapping.instantiation.model;

import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTypeAdapter;

public class CreatorWithAdapterContainer {

    private final StringWrapper stringWrapper;

    @JsonbCreator
    public CreatorWithAdapterContainer(@JsonbProperty("instance")
                                       @JsonbTypeAdapter(SimpleStringAdapter.class) StringWrapper stringWrapper) {
        this.stringWrapper = stringWrapper;
    }

    public StringWrapper getStringWrapper() {
        return stringWrapper;
    }

    public static class StringWrapper {

        private final String wrapped;

        private StringWrapper(String wrapped) {
            this.wrapped = wrapped;
        }

        public String getWrapped() {
            return wrapped;
        }

    }

    public static class SimpleStringAdapter implements JsonbAdapter<StringWrapper, String> {

        @Override
        public String adaptToJson(StringWrapper obj) {
            return obj.getWrapped();
        }

        @Override
        public StringWrapper adaptFromJson(String obj) {
            return new StringWrapper(obj);
        }
    }

}
