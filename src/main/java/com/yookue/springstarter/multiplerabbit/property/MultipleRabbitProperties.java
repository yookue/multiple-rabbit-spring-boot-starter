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

package com.yookue.springstarter.multiplerabbit.property;


import java.io.Serializable;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import com.yookue.springstarter.multiplerabbit.enumeration.RabbitExchangeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Properties for multiple rabbit
 *
 * @author David Hsing
 */
@Getter
@Setter
@ToString
public class MultipleRabbitProperties extends RabbitProperties {
    private final DelayedExchange delayedExchange = new DelayedExchange();


    /**
     * Properties for delayed exchange of rabbit
     *
     * @author David Hsing
     */
    @Getter
    @Setter
    @ToString
    public static class DelayedExchange implements Serializable {
        private Boolean enabled;
        private String name;
        private RabbitExchangeType type = RabbitExchangeType.DIRECT;
        private Boolean durable = Boolean.TRUE;
        private Boolean autoDelete = Boolean.TRUE;
    }
}
