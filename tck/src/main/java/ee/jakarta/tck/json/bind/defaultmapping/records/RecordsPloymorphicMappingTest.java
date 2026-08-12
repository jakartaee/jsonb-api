/*
 * Copyright (c) 2026 Eclipse and/or its affiliates. All rights reserved.
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
package ee.jakarta.tck.json.bind.defaultmapping.records;

import java.time.LocalDate;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for verification of proper type inheritance handling for records based on annotation with property format.
 */
public class RecordsPloymorphicMappingTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    @Test
    public void testBasicSerialization() {
        DVD dvd = new DVD("The Matrix", "Keanu Reeves", 136);
        String dvdResult = """
                          {"@type":"dvd","lead":"Keanu Reeves","lengthMinutes":136,"title":"The Matrix"}\
                          """;
        String jsonString = jsonb.toJson(dvd);
        assertThat("Failed to serialize DVD record correctly.",
                   jsonString, is(dvdResult));

        CD cd = new CD("Thriller", "Michael Jackson", 2520);
        String cdResult = """
                          {"@type":"cd","artist":"Michael Jackson","lengthSeconds":2520,"title":"Thriller"}\
                          """;
        jsonString = jsonb.toJson(cd);
        assertThat("Failed to serialize CD record correctly",
                   jsonString, is(cdResult));
    }

    @Test
    public void testBasicDeserialization() {
        String dvdInput = """
                          {"@type":"dvd","title":"Inception","lead":"Leonardo DiCaprio","lengthMinutes":148}\
                          """;
        Media dvd = jsonb.fromJson(dvdInput, Media.class);
        assertThat("Incorrectly deserialized to the type. Expected was DVD instance. Got instance of class " + dvd.getClass(),
                   dvd, instanceOf(DVD.class));
        assertThat("Incorrectly deserialized field of the DVD instance. Field \"title\" should have been \"Inception\".",
                   ((DVD) dvd).title(), is("Inception"));
        assertThat("Incorrectly deserialized field of the DVD instance. Field \"lengthMinutes\" should have been 148.",
                   ((DVD) dvd).lengthMinutes(), is(148));

        String cdInput = """
                         {"@type":"cd","title":"Abbey Road","artist":"The Beatles","lengthSeconds":2820}\
                         """;
        Media cd = jsonb.fromJson(cdInput, Media.class);
        assertThat("Incorrectly deserialized to the type. Expected was CD instance. Got instance of class " + cd.getClass(),
                   cd, instanceOf(CD.class));
        assertThat("Incorrectly deserialized field of the CD instance. Field \"artist\" should have been \"The Beatles\".",
                   ((CD) cd).artist(), is("The Beatles"));
        assertThat("Incorrectly deserialized field of the CD instance. Field \"lengthSeconds\" should have been 2820.",
                   ((CD) cd).lengthSeconds(), is(2820));
    }

    @Test
    public void testUnknownAliasDeserialization() {
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{\"@type\":\"book\",\"title\":\"Unknown\"}", Media.class),
                     "Deserialization should fail. Alias \"book\" is not valid alias of the interface Media.");
    }

    @Test
    public void testCreatorDeserialization() {
        String releaseInput = """
                              {"@release":"dated","releaseDate":"15-03-2021"}\
                              """;
        Release release = jsonb.fromJson(releaseInput, Release.class);
        assertThat("Incorrectly deserialized according to the type information. Expected was DatedRelease instance. "
                           + "Got instance of class " + release.getClass(),
                   release, instanceOf(DatedRelease.class));
    }

    @Test
    public void testArraySerialization() {
        String expected = """
                          [\
                          {"@type":"dvd","lead":"Actor1","lengthMinutes":120,"title":"Movie1"},\
                          {"@type":"cd","artist":"Artist1","lengthSeconds":3600,"title":"Album1"},\
                          {"@type":"dvd","lead":"Actor2","lengthMinutes":90,"title":"Movie2"}\
                          ]\
                          """;
        Media[] mediaArray = new Media[] {
            new DVD("Movie1", "Actor1", 120),
            new CD("Album1", "Artist1", 3600),
            new DVD("Movie2", "Actor2", 90)
        };
        String jsonString = jsonb.toJson(mediaArray);
        assertThat("Array values were not properly serialized with type information.",
                   jsonString, is(expected));
    }

    @Test
    public void testArrayDeserialization() {
        String mediaInput = """
                          [\
                          {"@type":"dvd","lead":"Actor1","lengthMinutes":120,"title":"Movie1"},\
                          {"@type":"cd","artist":"Artist1","lengthSeconds":3600,"title":"Album1"},\
                          {"@type":"dvd","lead":"Actor2","lengthMinutes":90,"title":"Movie2"}\
                          ]\
                          """;
        Media[] mediaArray = jsonb.fromJson(mediaInput, Media[].class);
        assertThat("Array should have exactly 3 values.", mediaArray, arrayWithSize(3));
        assertThat("Array value at index 0 was incorrectly deserialized according to the type information. "
                           + "Expected was DVD instance. Got instance of class " + mediaArray[0].getClass(),
                   mediaArray[0], instanceOf(DVD.class));
        assertThat("Array value at index 1 was incorrectly deserialized  according to the type information. "
                           + "Expected was CD instance. Got instance of class " + mediaArray[1].getClass(),
                   mediaArray[1], instanceOf(CD.class));
        assertThat("Array value at index 2 was incorrectly deserialized according to the type information. "
                           + "Expected was DVD instance. Got instance of class " + mediaArray[2].getClass(),
                   mediaArray[2], instanceOf(DVD.class));
    }

    @JsonbTypeInfo(key = "@type", value = {
        @JsonbSubtype(alias = "dvd", type = DVD.class),
        @JsonbSubtype(alias = "cd", type = CD.class),
        @JsonbSubtype(alias = "vinyl", type = Vinyl.class)
    })
    public interface Media {
    }

    public static record DVD(String title, String lead, int lengthMinutes) implements Media {
    }

    public static record CD(String title, String artist, int lengthSeconds) implements Media {
    }

    public static record Vinyl(String title, String artist, int tracks) implements Media {
    }

    @JsonbTypeInfo(key = "@release", value = {
            @JsonbSubtype(alias = "dated", type = DatedRelease.class)
    })
    public interface Release {
    }

    public static record DatedRelease(LocalDate releaseDate) implements Release {
        @JsonbCreator
        public DatedRelease(@JsonbProperty("releaseDate") @JsonbDateFormat(value = "dd-MM-yyyy", locale = "nl-NL") LocalDate releaseDate) {
            this.releaseDate = releaseDate;
        }
    }

}
