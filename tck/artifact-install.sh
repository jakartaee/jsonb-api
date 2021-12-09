#!/usr/bin/env bash

##script to install the artifact directory contents into a local maven repository

if [[ $1 =~ ^[0-9]+\.[0-9]+\.[0-9]+.*$ ]]; then
  VERSION="$1"
else
  VERSION="3.0.0-SNAPSHOT"
fi

#jar
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=jakarta.json.bind-tck-"$VERSION".jar -DgroupId=jakarta.json.bind \
-DartifactId=jakarta.json.bind-tck -Dversion="$VERSION" -Dpackaging=jar


#jar
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=jakarta.json.bind-tck-"$VERSION".jar -DgroupId=jakarta.json.bind \
-DartifactId=jakarta.json.bind-tck-sources -Dversion="$VERSION" -Dpackaging=jar