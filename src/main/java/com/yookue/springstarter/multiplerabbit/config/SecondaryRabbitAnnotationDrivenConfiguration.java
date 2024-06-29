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


import javax.annotation.Nullable;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.amqp.DirectRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitConfigurationUtils;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.yookue.commonplexus.springcondition.annotation.ConditionalOnAllProperties;
import lombok.NonNull;


/**
 * Secondary configuration for annotation driven rabbit
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnAllProperties(value = {
    @ConditionalOnProperty(prefix = "spring.multiple-rabbit", name = "enabled", havingValue = "true", matchIfMissing = true),
    @ConditionalOnProperty(prefix = SecondaryRabbitAutoConfiguration.PROPERTIES_PREFIX, name = "host")
})
@ConditionalOnClass(value = EnableRabbit.class)
@AutoConfigureAfter(value = {PrimaryRabbitAnnotationDrivenConfiguration.class, SecondaryRabbitAutoConfiguration.class})
@AutoConfigureBefore(value = RabbitAutoConfiguration.class)
public class SecondaryRabbitAnnotationDrivenConfiguration {
    public static final String MESSAGE_RECOVERER = "secondaryRabbitMessageRecoverer";    // $NON-NLS-1$
    public static final String SIMPLE_CONTAINER_FACTORY_CONFIGURER = "secondaryRabbitSimpleListenerContainerFactoryConfigurer";    // $NON-NLS-1$
    public static final String DIRECT_CONTAINER_FACTORY_CONFIGURER = "secondaryRabbitDirectListenerContainerFactoryConfigurer";    // $NON-NLS-1$
    public static final String CONTAINER_FACTORY = "secondaryRabbitListenerContainerFactory";    // $NON-NLS-1$

    @Bean(name = SIMPLE_CONTAINER_FACTORY_CONFIGURER)
    @ConditionalOnProperty(prefix = SecondaryRabbitAutoConfiguration.PROPERTIES_PREFIX, name = "listener.type", havingValue = "simple", matchIfMissing = true)
    @ConditionalOnBean(name = SecondaryRabbitAutoConfiguration.RABBIT_PROPERTIES, value = RabbitProperties.class)
    @ConditionalOnMissingBean(name = SIMPLE_CONTAINER_FACTORY_CONFIGURER)
    public SimpleRabbitListenerContainerFactoryConfigurer simpleRabbitListenerContainerFactoryConfigurer(
        @Qualifier(value = SecondaryRabbitAutoConfiguration.RABBIT_PROPERTIES) @NonNull RabbitProperties properties,
        @Autowired(required = false) @Qualifier(value = SecondaryRabbitAutoConfiguration.MESSAGE_CONVERTER) @Nullable MessageConverter converter,
        @Autowired(required = false) @Qualifier(value = MESSAGE_RECOVERER) @Nullable MessageRecoverer recoverer,
        @Autowired(required = false) @Qualifier(value = SecondaryRabbitAutoConfiguration.RETRY_TEMPLATE_CUSTOMIZER) @Nullable RabbitRetryTemplateCustomizer customizer) {
        return RabbitConfigurationUtils.simpleRabbitListenerContainerFactoryConfigurer(properties, converter, recoverer, customizer);
    }

    @Bean(name = CONTAINER_FACTORY)
    @ConditionalOnProperty(prefix = SecondaryRabbitAutoConfiguration.PROPERTIES_PREFIX, name = "listener.type", havingValue = "simple", matchIfMissing = true)
    @ConditionalOnBean(name = {SIMPLE_CONTAINER_FACTORY_CONFIGURER, SecondaryRabbitAutoConfiguration.CONNECTION_FACTORY})
    @ConditionalOnMissingBean(name = CONTAINER_FACTORY)
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
        @Qualifier(value = SIMPLE_CONTAINER_FACTORY_CONFIGURER) @NonNull SimpleRabbitListenerContainerFactoryConfigurer configurer,
        @Qualifier(value = SecondaryRabbitAutoConfiguration.CONNECTION_FACTORY) @NonNull ConnectionFactory factory) {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(containerFactory, factory);
        return containerFactory;
    }

    @Bean(name = DIRECT_CONTAINER_FACTORY_CONFIGURER)
    @ConditionalOnProperty(prefix = SecondaryRabbitAutoConfiguration.PROPERTIES_PREFIX, name = "listener.type", havingValue = "direct")
    @ConditionalOnBean(name = SecondaryRabbitAutoConfiguration.RABBIT_PROPERTIES, value = RabbitProperties.class)
    @ConditionalOnMissingBean(name = DIRECT_CONTAINER_FACTORY_CONFIGURER)
    public DirectRabbitListenerContainerFactoryConfigurer directRabbitListenerContainerFactoryConfigurer(
        @Qualifier(value = SecondaryRabbitAutoConfiguration.RABBIT_PROPERTIES) @NonNull RabbitProperties properties,
        @Autowired(required = false) @Qualifier(value = SecondaryRabbitAutoConfiguration.MESSAGE_CONVERTER) @Nullable MessageConverter converter,
        @Autowired(required = false) @Qualifier(value = MESSAGE_RECOVERER) @Nullable MessageRecoverer recoverer,
        @Autowired(required = false) @Qualifier(value = SecondaryRabbitAutoConfiguration.RETRY_TEMPLATE_CUSTOMIZER) @Nullable RabbitRetryTemplateCustomizer customizer) {
        return RabbitConfigurationUtils.directRabbitListenerContainerFactoryConfigurer(properties, converter, recoverer, customizer);
    }

    @Bean(name = CONTAINER_FACTORY)
    @ConditionalOnProperty(prefix = SecondaryRabbitAutoConfiguration.PROPERTIES_PREFIX, name = "listener.type", havingValue = "direct")
    @ConditionalOnBean(name = {DIRECT_CONTAINER_FACTORY_CONFIGURER, SecondaryRabbitAutoConfiguration.CONNECTION_FACTORY})
    @ConditionalOnMissingBean(name = CONTAINER_FACTORY)
    public DirectRabbitListenerContainerFactory directRabbitListenerContainerFactory(
        @Qualifier(value = DIRECT_CONTAINER_FACTORY_CONFIGURER) @NonNull DirectRabbitListenerContainerFactoryConfigurer configurer,
        @Qualifier(value = SecondaryRabbitAutoConfiguration.CONNECTION_FACTORY) @NonNull ConnectionFactory factory) {
        DirectRabbitListenerContainerFactory containerFactory = new DirectRabbitListenerContainerFactory();
        configurer.configure(containerFactory, factory);
        return containerFactory;
    }
}
