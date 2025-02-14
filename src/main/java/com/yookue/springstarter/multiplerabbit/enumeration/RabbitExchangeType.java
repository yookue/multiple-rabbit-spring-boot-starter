/*
 * Copyright (c) 2020 Yookue Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yookue.springstarter.multiplerabbit.enumeration;


import com.yookue.commonplexus.javaseutil.support.ValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Enumerations for rabbit exchange types
 *
 * @author David Hsing
 */
@AllArgsConstructor
@Getter
@SuppressWarnings("unused")
public enum RabbitExchangeType implements ValueEnum<String> {
    DIRECT("direct"),    // $NON-NLS-1$
    FANOUT("fanout"),    // $NON-NLS-1$
    HEADERS("headers"),    // $NON-NLS-1$
    TOPIC("topic");    // $NON-NLS-1$

    private final String value;
}
