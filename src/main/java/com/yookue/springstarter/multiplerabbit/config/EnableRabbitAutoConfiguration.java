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

package com.yookue.springstarter.multiplerabbit.config;


import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import com.rabbitmq.client.Channel;
import com.yookue.commonplexus.springcondition.annotation.ConditionalOnAnyProperties;


/**
 * Configuration for {@link org.springframework.amqp.rabbit.annotation.EnableRabbit}
 *
 * @author David Hsing
 * @see org.springframework.amqp.rabbit.annotation.EnableRabbit
 * @see org.springframework.amqp.rabbit.annotation.MultiRabbitBootstrapConfiguration
 * @see org.springframework.amqp.rabbit.annotation.RabbitBootstrapConfiguration
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.multiple-rabbit", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnAnyProperties(value = {
    @ConditionalOnProperty(prefix = PrimaryRabbitAutoConfiguration.PROPERTIES_PREFIX, name = "host"),
    @ConditionalOnProperty(prefix = SecondaryRabbitAutoConfiguration.PROPERTIES_PREFIX, name = "host"),
    @ConditionalOnProperty(prefix = TertiaryRabbitAutoConfiguration.PROPERTIES_PREFIX, name = "host")
})
@ConditionalOnClass(value = {RabbitTemplate.class, Channel.class})
@AutoConfigureBefore(value = RabbitAutoConfiguration.class)
@EnableRabbit
@ConditionalOnMissingBean(name = "org.springframework.amqp.rabbit.config.internalRabbitListenerAnnotationProcessor")
public class EnableRabbitAutoConfiguration {
}
