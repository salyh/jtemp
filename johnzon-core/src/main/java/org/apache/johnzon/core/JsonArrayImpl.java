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

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

class JsonArrayImpl extends AbstractList<JsonValue> implements JsonArray, Serializable {
    private final Integer hashCode = null;
    private final JsonValue[] unmodifieableBackingList;

    JsonArrayImpl(final JsonValue[] backingList) {
        super();
        this.unmodifieableBackingList = backingList;
    }

    private <T> T value(final int idx, final Class<T> type) {
        if (idx > unmodifieableBackingList.length) {
            throw new IndexOutOfBoundsException(idx + "/" + unmodifieableBackingList.length);
        }
        return type.cast(unmodifieableBackingList[idx]);
    }

    @Override
    public JsonObject getJsonObject(final int index) {
        return value(index, JsonObject.class);
    }

    @Override
    public JsonArray getJsonArray(final int index) {
        return value(index, JsonArray.class);
    }

    @Override
    public JsonNumber getJsonNumber(final int index) {
        return value(index, JsonNumber.class);
    }

    @Override
    public JsonString getJsonString(final int index) {
        return value(index, JsonString.class);
    }

    @Override
    public <T extends JsonValue> List<T> getValuesAs(final Class<T> clazz) {
        return (List<T>) Arrays.asList(unmodifieableBackingList);
    }

    @Override
    public String getString(final int index) {
        return value(index, JsonString.class).getString();
    }

    @Override
    public String getString(final int index, final String defaultValue) {
        JsonValue val = null;

        if (index > unmodifieableBackingList.length - 1 || !((val = get(index)) instanceof JsonString)) {
            return defaultValue;
        } else {
            return JsonString.class.cast(val).getString();
        }
    }

    @Override
    public int getInt(final int index) {
        return value(index, JsonNumber.class).intValue();
    }

    @Override
    public int getInt(final int index, final int defaultValue) {
        JsonValue val = null;

        if (index > unmodifieableBackingList.length - 1 || !((val = get(index)) instanceof JsonNumber)) {
            return defaultValue;
        } else {
            return JsonNumber.class.cast(val).intValue();
        }
    }

    @Override
    public boolean getBoolean(final int index) {
        final JsonValue val = value(index, JsonValue.class);

        if (val == JsonValue.TRUE) {
            return true;
        } else if (val == JsonValue.FALSE) {
            return false;
        } else {
            throw new ClassCastException();
        }

    }

    @Override
    public boolean getBoolean(final int index, final boolean defaultValue) {

        if (index > unmodifieableBackingList.length - 1) {
            return defaultValue;
        }

        final JsonValue val = get(index);

        if (val == JsonValue.TRUE) {
            return true;
        } else if (val == JsonValue.FALSE) {
            return false;
        } else {
            return defaultValue;
        }

    }

    @Override
    public boolean isNull(final int index) {
        return value(index, JsonValue.class) == JsonValue.NULL;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.ARRAY;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("[");

        for (int i = 0; i < unmodifieableBackingList.length; i++) {
            final JsonValue jsonValue = unmodifieableBackingList[i];
            if (JsonString.class.isInstance(jsonValue)) {
                builder.append(jsonValue.toString());
            } else {
                builder.append(jsonValue != JsonValue.NULL ? jsonValue.toString() : JsonChars.NULL);
            }
            // hasNext = it.hasNext();
            if (i != unmodifieableBackingList.length - 1) {
                builder.append(",");
            }
        }
        return builder.append(']').toString();
    }

    @Override
    public boolean equals(final Object obj) {
        return JsonArrayImpl.class.isInstance(obj)
                && Arrays.deepEquals(unmodifieableBackingList, (JsonArrayImpl.class.cast(obj).unmodifieableBackingList));
    }

    @Override
    public int hashCode() {
        Integer h = hashCode;
        if (h == null) {
            h = unmodifieableBackingList.hashCode();
            h = hashCode;
        }
        return h;
    }

    @Override
    public JsonValue get(final int index) {
        return unmodifieableBackingList[index];
    }

    @Override
    public int size() {
        return unmodifieableBackingList.length;
    }
}
