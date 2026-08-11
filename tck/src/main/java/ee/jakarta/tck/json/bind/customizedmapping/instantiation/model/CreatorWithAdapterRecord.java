/*
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

package ee.jakarta.tck.json.bind.customizedmapping.instantiation.model;

import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTypeAdapter;

/**
 * Used to verify that an @JsonbTypeAdapter can be used on a parameter of a 
 * custom constructor used by @JsonbCreator
 */
public record CreatorWithAdapterRecord(StringWrapper stringWrapper) {

    @JsonbCreator
    public CreatorWithAdapterRecord(
            @JsonbProperty("instance") @JsonbTypeAdapter(SimpleStringAdapter.class) StringWrapper stringWrapper) {
        this.stringWrapper = stringWrapper;
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
