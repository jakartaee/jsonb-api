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

package ee.jakarta.tck.json.bind.defaultmapping.attributeorder.model;

public class ExtendedContainer extends SimpleContainer {
    private short shortInstance;

    private float floatInstance;

    public short getShortInstance() {
        return shortInstance;
    }

    public void setShortInstance(short shortInstance) {
        this.shortInstance = shortInstance;
        if (intInstance == 1) {
            intInstance = 2;
        }
    }

    public float getFloatInstance() {
        return floatInstance;
    }

    public void setFloatInstance(float floatInstance) {
        this.floatInstance = floatInstance;
        if (intInstance == 3) {
            intInstance = 4;
        }
    }

    @Override
    public void setStringInstance(String stringInstance) {
        this.stringInstance = stringInstance;
        if (intInstance == 2) {
            intInstance = 3;
        }
    }

    @Override
    public void setIntInstance(int intInstance) {
        this.intInstance = intInstance;
    }

    @Override
    public void setLongInstance(long longInstance) {
        this.longInstance = longInstance;
        if (intInstance == 4) {
            intInstance = 5;
        }
    }
}
