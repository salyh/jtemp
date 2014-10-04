/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.johnzon.core;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParsingException;

class JsonReaderImpl implements JsonReader {
    private final JsonParser parser;
    private boolean closed = false;
    private final JsonBuilderFactory builderFactory;

    JsonReaderImpl(final JsonParser parser, JsonBuilderFactory builderFactory) {
        this.parser = parser;
        this.builderFactory = builderFactory;
    }

    @Override
    public JsonStructure read() {

        checkClosed();

        if (!parser.hasNext()) {
            throw new IllegalStateException("Nothing to read");
        }
        switch (parser.next()) {
            case START_OBJECT:
                final JsonObjectBuilder objectBuilder = builderFactory.createObjectBuilder();
                parseObject(objectBuilder);
                if (parser.hasNext()) {
                    throw new JsonParsingException("Expected end of file", parser.getLocation());
                }
                close();
                return objectBuilder.build();
            case START_ARRAY:
                final JsonArrayBuilder arrayBuilder = builderFactory.createArrayBuilder();
                parseArray(arrayBuilder);
                if (parser.hasNext()) {
                    throw new JsonParsingException("Expected end of file", parser.getLocation());
                }
                close();
                return arrayBuilder.build();
            default:
                close();
                throw new JsonParsingException("Unknown structure: " + parser.next(), parser.getLocation());
        }

    }

    @Override
    public JsonObject readObject() {
        return JsonObject.class.cast(read());
    }

    @Override
    public JsonArray readArray() {
        return JsonArray.class.cast(read());
    }

    @Override
    public void close() {

        if (!closed) {
            closed = true;
            parser.close();
        }

    }

    private void parseObject(final JsonObjectBuilder builder) {
        String key = null;
        while (parser.hasNext()) {
            final JsonParser.Event next = parser.next();
            switch (next) {
                case KEY_NAME:
                    key = parser.getString();
                    break;

                case VALUE_STRING:
                    builder.add(key, new JsonStringImpl(parser.getString()));
                    break;

                case START_OBJECT:
                    JsonObjectBuilder subObject = null;
                    parseObject(subObject = builderFactory.createObjectBuilder());
                    builder.add(key, subObject);
                    break;

                case START_ARRAY:
                    JsonArrayBuilder subArray = null;
                    parseArray(subArray = builderFactory.createArrayBuilder());
                    builder.add(key, subArray);
                    break;

                case VALUE_NUMBER:
                    if (parser.isIntegralNumber()) {
                        builder.add(key, new JsonLongImpl(parser.getLong()));
                    } else {
                        builder.add(key, new JsonNumberImpl(parser.getBigDecimal()));
                    }
                    break;

                case VALUE_NULL:
                    builder.addNull(key);
                    break;

                case VALUE_TRUE:
                    builder.add(key, true);
                    break;

                case VALUE_FALSE:
                    builder.add(key, false);
                    break;

                case END_OBJECT:
                    return;

                case END_ARRAY:
                    throw new JsonParsingException("']', shouldn't occur", parser.getLocation());

                default:
                    throw new JsonParsingException(next.name() + ", shouldn't occur", parser.getLocation());
            }
        }
    }

    private void parseArray(final JsonArrayBuilder builder) {
        while (parser.hasNext()) {
            final JsonParser.Event next = parser.next();
            switch (next) {
                case VALUE_STRING:
                    builder.add(new JsonStringImpl(parser.getString()));
                    break;

                case VALUE_NUMBER:
                    if (parser.isIntegralNumber()) {
                        builder.add(new JsonLongImpl(parser.getLong()));
                    } else {
                        builder.add(new JsonNumberImpl(parser.getBigDecimal()));
                    }
                    break;

                case START_OBJECT:
                    JsonObjectBuilder subObject = null;
                    parseObject(subObject = builderFactory.createObjectBuilder());
                    builder.add(subObject);
                    break;

                case START_ARRAY:
                    JsonArrayBuilder subArray = null;
                    parseArray(subArray = builderFactory.createArrayBuilder());
                    builder.add(subArray);
                    break;

                case END_ARRAY:
                    return;

                case VALUE_NULL:
                    builder.addNull();
                    break;

                case VALUE_TRUE:
                    builder.add(true);
                    break;

                case VALUE_FALSE:
                    builder.add(false);
                    break;

                case KEY_NAME:
                    throw new JsonParsingException("array doesn't have keys", parser.getLocation());

                case END_OBJECT:
                    throw new JsonParsingException("'}', shouldn't occur", parser.getLocation());

                default:
                    throw new JsonParsingException(next.name() + ", shouldn't occur", parser.getLocation());
            }
        }
    }

    private void checkClosed() {
        if (closed) {
            throw new IllegalStateException("read(), readObject(), readArray() or close() method was already called");
        }

    }
}
