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
package org.apache.johnzon.jaxrs;

import org.apache.johnzon.mapper.Mapper;
import org.apache.johnzon.mapper.MapperBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.MediaType.WILDCARD;

@Provider
@Produces(WILDCARD)
@Consumes(WILDCARD)
public class JohnzonProvider<T> extends DelegateProvider<T> {
    public JohnzonProvider(final Mapper mapper) {
        super(new JohnzonMessageBodyReader<T>(mapper), new JohnzonMessageBodyWriter<T>(mapper));
    }

    public JohnzonProvider() {
        this(new MapperBuilder().setDoCloseOnStreams(false).build());
    }
}
