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

package ee.jakarta.tck.json.bind.customizedmapping.dateformat.model;

import java.util.Date;

import jakarta.json.bind.annotation.JsonbDateFormat;

import ee.jakarta.tck.json.bind.TypeContainer;

public class AnnotatedAccessorsDateContainer implements TypeContainer<Date> {
    private Date instance;

    @Override
    @JsonbDateFormat(value = "E DD MMM yyyy HH:mm:ss z", locale = "it")
    public Date getInstance() {
        return instance;
    }

    @Override
    @JsonbDateFormat(value = "E DD MMM yyyy HH:mm:ss z", locale = "de")
    public void setInstance(Date instance) {
        this.instance = instance;
    }
}
