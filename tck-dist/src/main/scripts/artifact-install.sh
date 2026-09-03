#!/usr/bin/env bash

# Copyright (c) 2022, 2026 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Public License v. 2.0, which is available at
# http://www.eclipse.org/legal/epl-2.0.
#
# This Source Code may also be made available under the following Secondary
# Licenses when the conditions for such availability set forth in the
# Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
# version 2 with the GNU Classpath Exception, which is available at
# https://www.gnu.org/software/classpath/license.html.
#
# SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

##script to install the artifact directory contents into a local maven repository

if [[ $1 =~ ^[0-9]+\.[0-9]+\.[0-9]+.*$ ]]; then
  VERSION="$1"
else
  VERSION="3.1.0"
fi

# Parent pom
mvn org.apache.maven.plugins:maven-install-plugin:3.1.4:install-file \
-Dfile=jakarta.json.bind-parent-"$VERSION".pom -DgroupId=jakarta.json.bind \
-DartifactId=jakarta.json.bind-parent -Dversion="$VERSION" -Dpackaging=pom

# API jar
mvn org.apache.maven.plugins:maven-install-plugin:3.1.4:install-file \
-Dfile=jakarta.json.bind-api-"$VERSION".jar -DgroupId=jakarta.json.bind \
-DartifactId=jakarta.json.bind-api -Dversion="$VERSION" -Dpackaging=jar

# TCK jar
mvn org.apache.maven.plugins:maven-install-plugin:3.1.4:install-file \
-Dfile=jakarta.json.bind-tck-"$VERSION".jar -DgroupId=jakarta.json.bind \
-DartifactId=jakarta.json.bind-tck-sources -Dversion="$VERSION" -Dpackaging=jar