/*
 * Copyright (c) 2017, 2022 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package ee.jakarta.tck.json.bind.defaultmapping.specifictypes.model;

import java.util.Arrays;
import java.util.Optional;

import ee.jakarta.tck.json.bind.TypeContainer;

public class OptionalArrayContainer
        implements TypeContainer<Optional<String>[]> {
    private Optional<String>[] instance;

    @Override
    public Optional<String>[] getInstance() {
        return instance;
    }

    @Override
    public void setInstance(Optional<String>[] instance) {
        this.instance = instance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionalArrayContainer)) {
            return false;
        }

        OptionalArrayContainer that = (OptionalArrayContainer) o;

        return Arrays.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(instance);
    }
}
