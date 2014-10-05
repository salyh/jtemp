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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;

class JsonBuilderFactoryImpl implements JsonBuilderFactory {
    public static final String ALLOW_DUPLICATE_KEYS = "org.apache.johnzon.allow-duplicate-keys";
    private final Map<String, Object> internalConfig = new HashMap<String, Object>();
    private static final String[] SUPPORTED_CONFIG_KEYS = new String[] {

    ALLOW_DUPLICATE_KEYS

    };

    JsonBuilderFactoryImpl(final Map<String, ?> config) {
        if (config != null) {

            for (final String configKey : SUPPORTED_CONFIG_KEYS) {
                if (config.containsKey(configKey)) {
                    internalConfig.put(configKey, config.get(configKey));
                }
            }
        }

        if (internalConfig.containsKey(ALLOW_DUPLICATE_KEYS)) {
            this.allowDuplicates = Boolean.TRUE.equals(internalConfig.get(ALLOW_DUPLICATE_KEYS))
                    || "true".equals(internalConfig.get(ALLOW_DUPLICATE_KEYS));
        } else {
            this.allowDuplicates = true; // TODO testcase for false
        }
    }

    private boolean allowDuplicates;

    @Override
    public JsonObjectBuilder createObjectBuilder() {
        return new JsonObjectBuilderImpl(allowDuplicates);
    }

    @Override
    public JsonArrayBuilder createArrayBuilder() {
        return new JsonArrayBuilderImpl();
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return Collections.unmodifiableMap(internalConfig);
    }
}
