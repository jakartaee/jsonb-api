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

package ee.jakarta.tck.json.bind.customizedmapping.adapters.model.adapter;

import ee.jakarta.tck.json.bind.customizedmapping.adapters.model.Animal;
import ee.jakarta.tck.json.bind.customizedmapping.adapters.model.Cat;
import ee.jakarta.tck.json.bind.customizedmapping.adapters.model.Dog;

import static ee.jakarta.tck.json.bind.customizedmapping.adapters.model.adapter.AnimalJson.TYPE.CAT;
import static ee.jakarta.tck.json.bind.customizedmapping.adapters.model.adapter.AnimalJson.TYPE.DOG;
import static ee.jakarta.tck.json.bind.customizedmapping.adapters.model.adapter.AnimalJson.TYPE.GENERIC;

public class AnimalIdentifier {
    public AnimalJson.TYPE getType(Animal animal) {
        if (Cat.class.isAssignableFrom(animal.getClass())) {
            return CAT;
        } else if (Dog.class.isAssignableFrom(animal.getClass())) {
            return DOG;
        } else {
            return GENERIC;
        }
    }
}
