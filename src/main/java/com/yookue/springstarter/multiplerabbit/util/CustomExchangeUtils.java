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

package com.yookue.springstarter.multiplerabbit.util;


import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.CustomExchange;
import com.yookue.springstarter.multiplerabbit.constant.CustomExchangeConst;
import com.yookue.springstarter.multiplerabbit.enumeration.RabbitExchangeType;


/**
 * Utilities for {@link org.springframework.amqp.core.CustomExchange}
 *
 * @author David Hsing
 * @see org.springframework.amqp.core.CustomExchange
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public abstract class CustomExchangeUtils {
    /**
     * Returns a delayed message exchange with the rabbit plugin of "rabbitmq-delayed-message-exchange"
     *
     * @param admin an {@link AmqpAdmin} instance that the message exchange will be created with
     * @param name the name of the exchange
     * @param type custom exchange types are allowed by the AMQP specification, and their names should start with "x-" (but this is not enforced)
     * @param durable true if declaring a durable exchange (the exchange will survive a server restart)
     * @param autoDelete true if the server should delete the exchange when it is no longer in use
     *
     * @return a delayed message exchange with the rabbit plugin of "rabbitmq-delayed-message-exchange"
     *
     * @reference "https://github.com/rabbitmq/rabbitmq-delayed-message-exchange"
     */
    @Nullable
    @SuppressWarnings({"JavadocDeclaration", "JavadocLinkAsPlainText"})
    public static CustomExchange delayedMessageExchange(@Nonnull AmqpAdmin admin, @Nonnull String name, @Nullable RabbitExchangeType type, boolean durable, boolean autoDelete) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        Map<String, Object> args = new LinkedHashMap<>(1);
        args.put(CustomExchangeConst.X_DELAYED_TYPE, (type != null) ? type.getValue() : RabbitExchangeType.DIRECT.getValue());
        CustomExchange exchange = new CustomExchange(name, CustomExchangeConst.X_DELAYED_MESSAGE, durable, autoDelete, args);
        admin.declareExchange(exchange);
        return exchange;
    }
}
