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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.junit.Test;

public class JsonObjectBuilderImplTest {
    @Test
    public void build() {
        final JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("a", "b");
        assertEquals("{\"a\":\"b\"}", builder.build().toString());
    }
    
    @Test
    public void dups() {
        final JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("a", "b");
        builder.add("a", "c");
        JsonObject jo = builder.build();
        assertEquals("{\"a\":\"c\"}", jo.toString());
        assertEquals(1,  jo.size());
    }
    
    @Test(expected=JsonException.class)
    public void dupsNotAllowed() {
        final JsonObjectBuilder builder = Json.createBuilderFactory(new HashMap<String, Object>() {
            {
                put(JsonBuilderFactoryImpl.ALLOW_DUPLICATE_KEYS, "false");
            }
        }).createObjectBuilder();
        builder.add("a", "b");
        builder.add("a", "c");
        
    }
}
