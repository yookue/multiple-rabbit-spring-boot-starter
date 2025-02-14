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


import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.amqp.CachingConnectionFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.ConnectionFactoryCustomizer;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitConfigurationUtils;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionFactoryBeanConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.CredentialsProvider;
import com.rabbitmq.client.impl.CredentialsRefreshService;
import com.yookue.commonplexus.springcondition.annotation.ConditionalOnAllProperties;
import com.yookue.springstarter.multiplerabbit.property.MultipleRabbitProperties;
import com.yookue.springstarter.multiplerabbit.util.CustomExchangeUtils;
import lombok.NonNull;


/**
 * Primary configuration for rabbit
 *
 * @author David Hsing
 * @see org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnAllProperties(value = {
    @ConditionalOnProperty(prefix = "spring.multiple-rabbit", name = "enabled", havingValue = "true", matchIfMissing = true),
    @ConditionalOnProperty(prefix = PrimaryRabbitAutoConfiguration.PROPERTIES_PREFIX, name = "host")
})
@ConditionalOnClass(value = {RabbitTemplate.class, Channel.class})
@AutoConfigureAfter(value = EnableRabbitAutoConfiguration.class)
@AutoConfigureBefore(value = RabbitAutoConfiguration.class)
public class PrimaryRabbitAutoConfiguration {
    public static final String PROPERTIES_PREFIX = "spring.multiple-rabbit.primary";    // $NON-NLS-1$
    public static final String RABBIT_PROPERTIES = "primaryRabbitProperties";    // $NON-NLS-1$
    public static final String CONNECTION_DETAILS = "primaryRabbitConnectionDetails";    // $NON-NLS-1$
    public static final String CREDENTIALS_PROVIDER = "primaryRabbitCredentialsProvider";    // $NON-NLS-1$
    public static final String CREDENTIALS_REFRESH_SERVICE = "primaryRabbitCredentialsRefreshService";    // $NON-NLS-1$
    public static final String SSL_BUNDLES = "primaryRabbitSslBundles";    // $NON-NLS-1$
    public static final String CONNECTION_FACTORY_BEAN_CONFIGURER = "primaryRabbitConnectionFactoryBeanConfigurer";
    public static final String CONNECTION_NAME_STRATEGY = "primaryRabbitConnectionNameStrategy";    // $NON-NLS-1$
    public static final String CACHING_CONNECTION_FACTORY_CONFIGURER = "primaryRabbitCachingConnectionFactoryConfigurer";    // $NON-NLS-1$
    public static final String CONNECTION_FACTORY_CUSTOMIZER = "primaryRabbitConnectionFactoryCustomizer";    // $NON-NLS-1$
    public static final String CONNECTION_FACTORY = "primaryRabbitConnectionFactory";    // $NON-NLS-1$
    public static final String MESSAGE_CONVERTER = "primaryRabbitMessageConverter";    // $NON-NLS-1$
    public static final String RETRY_TEMPLATE_CUSTOMIZER = "primaryRabbitRetryTemplateCustomizer";    // $NON-NLS-1$
    public static final String RABBIT_TEMPLATE_CONFIGURER = "primaryRabbitTemplateConfigurer";    // $NON-NLS-1$
    public static final String RABBIT_TEMPLATE_CUSTOMIZER = "primaryRabbitTemplateCustomizer";    // $NON-NLS-1$
    public static final String RABBIT_TEMPLATE = "primaryRabbitTemplate";    // $NON-NLS-1$
    public static final String RABBIT_MESSAGING_TEMPLATE = "primaryRabbitMessagingTemplate";    // $NON-NLS-1$
    public static final String AMQP_ADMIN = "primaryRabbitAmqpAdmin";    // $NON-NLS-1$
    public static final String DELAYED_EXCHANGE = "primaryRabbitDelayedExchange";    // $NON-NLS-1$

    @Primary
    @Bean(name = RABBIT_PROPERTIES)
    @ConditionalOnMissingBean(name = RABBIT_PROPERTIES)
    @ConfigurationProperties(prefix = PROPERTIES_PREFIX)
    public MultipleRabbitProperties rabbitProperties() {
        return new MultipleRabbitProperties();
    }

    @Primary
    @Bean(name = CONNECTION_DETAILS)
    @ConditionalOnBean(name = RABBIT_PROPERTIES)
    @ConditionalOnMissingBean(name = CONNECTION_DETAILS)
    public RabbitConnectionDetails rabbitConnectionDetails(@Qualifier(value = RABBIT_PROPERTIES) @NonNull MultipleRabbitProperties properties) {
        return RabbitConfigurationUtils.rabbitConnectionDetails(properties);
    }

    @Primary
    @Bean(name = CONNECTION_FACTORY_BEAN_CONFIGURER)
    @ConditionalOnBean(name = RABBIT_PROPERTIES)
    @ConditionalOnMissingBean(name = CONNECTION_FACTORY_BEAN_CONFIGURER)
    public RabbitConnectionFactoryBeanConfigurer rabbitConnectionFactoryBeanConfigurer(@Qualifier(value = RABBIT_PROPERTIES) @NonNull MultipleRabbitProperties properties, @NonNull ResourceLoader loader,
        @Autowired(required = false) @Qualifier(value = CONNECTION_DETAILS) @Nullable RabbitConnectionDetails details,
        @Autowired(required = false) @Qualifier(value = CREDENTIALS_PROVIDER) @Nullable CredentialsProvider credentials,
        @Autowired(required = false) @Qualifier(value = CREDENTIALS_REFRESH_SERVICE) @Nullable CredentialsRefreshService refresh,
        @Autowired(required = false) @Qualifier(value = SSL_BUNDLES) @Nullable SslBundles bundles) {
        return RabbitConfigurationUtils.rabbitConnectionFactoryBeanConfigurer(properties, loader, details, credentials, refresh, bundles);
    }

    @Primary
    @Bean(name = CACHING_CONNECTION_FACTORY_CONFIGURER)
    @ConditionalOnBean(name = RABBIT_PROPERTIES)
    @ConditionalOnMissingBean(name = CACHING_CONNECTION_FACTORY_CONFIGURER)
    public CachingConnectionFactoryConfigurer cachingConnectionFactoryConfigurer(@Qualifier(value = RABBIT_PROPERTIES) @NonNull MultipleRabbitProperties properties,
        @Autowired(required = false) @Qualifier(value = CONNECTION_DETAILS) @Nullable RabbitConnectionDetails details,
        @Autowired(required = false) @Qualifier(value = CONNECTION_NAME_STRATEGY) @Nullable ConnectionNameStrategy strategy) {
        return RabbitConfigurationUtils.cachingConnectionFactoryConfigurer(properties, details, strategy);
    }

    @Primary
    @Bean(name = CONNECTION_FACTORY)
    @ConditionalOnBean(name = RABBIT_PROPERTIES)
    @ConditionalOnMissingBean(name = CONNECTION_FACTORY)
    public CachingConnectionFactory cachingConnectionFactory(@Qualifier(value = RABBIT_PROPERTIES) @NonNull MultipleRabbitProperties properties,
        @Qualifier(value = CONNECTION_FACTORY_BEAN_CONFIGURER) @NonNull RabbitConnectionFactoryBeanConfigurer rabbitConfigurer,
        @Qualifier(value = CACHING_CONNECTION_FACTORY_CONFIGURER) @NonNull CachingConnectionFactoryConfigurer cachingConfigurer,
        @Autowired(required = false) @Qualifier(value = CONNECTION_FACTORY_CUSTOMIZER) @Nullable ConnectionFactoryCustomizer customizer) throws Exception {
        return RabbitConfigurationUtils.cachingConnectionFactory(properties, rabbitConfigurer, cachingConfigurer, customizer);
    }

    @Primary
    @Bean(name = RABBIT_TEMPLATE_CONFIGURER)
    @ConditionalOnBean(name = RABBIT_PROPERTIES)
    @ConditionalOnMissingBean(name = RABBIT_TEMPLATE_CONFIGURER)
    public RabbitTemplateConfigurer rabbitTemplateConfigurer(@Qualifier(value = RABBIT_PROPERTIES) @NonNull MultipleRabbitProperties properties,
        @Autowired(required = false) @Qualifier(value = MESSAGE_CONVERTER) @Nullable MessageConverter converter,
        @Autowired(required = false) @Qualifier(value = RETRY_TEMPLATE_CUSTOMIZER) @Nullable RabbitRetryTemplateCustomizer customizer) {
        return RabbitConfigurationUtils.rabbitTemplateConfigurer(properties, converter, customizer);
    }

    @Primary
    @Bean(name = RABBIT_TEMPLATE)
    @ConditionalOnBean(name = {RABBIT_TEMPLATE_CONFIGURER, CONNECTION_FACTORY})
    @ConditionalOnMissingBean(name = RABBIT_TEMPLATE)
    public RabbitTemplate rabbitTemplate(@Qualifier(value = RABBIT_TEMPLATE_CONFIGURER) @NonNull RabbitTemplateConfigurer configurer,
        @Qualifier(value = CONNECTION_FACTORY) @NonNull ConnectionFactory factory,
        @Autowired(required = false) @Qualifier(value = RABBIT_TEMPLATE_CUSTOMIZER) @NonNull RabbitTemplateCustomizer customizer) {
        return RabbitConfigurationUtils.rabbitTemplate(configurer, factory, customizer);
    }

    @Primary
    @Bean(name = RABBIT_MESSAGING_TEMPLATE)
    @ConditionalOnBean(name = RABBIT_TEMPLATE, value = RabbitTemplate.class)
    @ConditionalOnMissingBean(name = RABBIT_MESSAGING_TEMPLATE)
    public RabbitMessagingTemplate rabbitMessagingTemplate(@Qualifier(value = RABBIT_TEMPLATE) @NonNull RabbitTemplate template) {
        return new RabbitMessagingTemplate(template);
    }

    @Primary
    @Bean(name = AMQP_ADMIN)
    @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "dynamic", matchIfMissing = true)
    @ConditionalOnBean(name = CONNECTION_FACTORY)
    @ConditionalOnMissingBean(name = AMQP_ADMIN)
    public AmqpAdmin amqpAdmin(@Qualifier(value = CONNECTION_FACTORY) @NonNull ConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

    @Primary
    @Bean(name = DELAYED_EXCHANGE)
    @ConditionalOnAllProperties(value = {
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX + ".delayed-exchange", name = "enabled", havingValue = "true", matchIfMissing = true),
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX + ".delayed-exchange", name = "name")
    })
    @ConditionalOnBean(name = AMQP_ADMIN)
    @ConditionalOnMissingBean(name = DELAYED_EXCHANGE)
    public CustomExchange delayedExchange(@Qualifier(value = AMQP_ADMIN) @Nonnull AmqpAdmin amqpAdmin, @Qualifier(value = RABBIT_PROPERTIES) @NonNull MultipleRabbitProperties properties) {
        MultipleRabbitProperties.DelayedExchange prop = properties.getDelayedExchange();
        return CustomExchangeUtils.delayedMessageExchange(amqpAdmin, prop.getName(), prop.getType(), BooleanUtils.isTrue(prop.getDurable()), BooleanUtils.isTrue(prop.getAutoDelete()));
    }
}
