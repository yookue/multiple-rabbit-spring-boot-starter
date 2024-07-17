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

package org.springframework.boot.autoconfigure.amqp;


import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.amqp.rabbit.config.ContainerCustomizer;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.core.io.ResourceLoader;
import com.rabbitmq.client.impl.CredentialsProvider;
import com.rabbitmq.client.impl.CredentialsRefreshService;
import com.yookue.commonplexus.springutil.support.SingletonObjectProvider;
import lombok.NonNull;


/**
 * Utilities for {@link org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration}
 *
 * @author David Hsing
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public abstract class RabbitConfigurationUtils extends RabbitAutoConfiguration {
    @Nonnull
    public static RabbitConnectionDetails rabbitConnectionDetails(@NonNull RabbitProperties properties) {
        return new PropertiesRabbitConnectionDetails(properties);
    }

    @Nonnull
    public static RabbitConnectionFactoryBeanConfigurer rabbitConnectionFactoryBeanConfigurer(@NonNull RabbitProperties properties, @NonNull ResourceLoader loader, @Nullable RabbitConnectionDetails details, @Nullable CredentialsProvider credentials, @Nullable CredentialsRefreshService refresh, @Nullable SslBundles bundles) {
        RabbitConnectionDetails alias = ObjectUtils.defaultIfNull(details, rabbitConnectionDetails(properties));
        return new RabbitConnectionFactoryCreator(properties).rabbitConnectionFactoryBeanConfigurer(loader, alias, SingletonObjectProvider.ofNullable(credentials), SingletonObjectProvider.ofNullable(refresh), SingletonObjectProvider.ofNullable(bundles));
    }

    @Nonnull
    public static CachingConnectionFactoryConfigurer cachingConnectionFactoryConfigurer(@NonNull RabbitProperties properties, @Nullable RabbitConnectionDetails details, @Nullable ConnectionNameStrategy strategy) {
        RabbitConnectionDetails alias = ObjectUtils.defaultIfNull(details, rabbitConnectionDetails(properties));
        return new RabbitConnectionFactoryCreator(properties).rabbitConnectionFactoryConfigurer(alias, SingletonObjectProvider.ofNullable(strategy));
    }

    @NonNull
    public static CachingConnectionFactory cachingConnectionFactory(@NonNull RabbitProperties properties, @NonNull RabbitConnectionFactoryBeanConfigurer rabbitConfigurer, @NonNull CachingConnectionFactoryConfigurer cachingConfigurer, @Nullable ConnectionFactoryCustomizer customizer) throws Exception {
        return new RabbitConnectionFactoryCreator(properties).rabbitConnectionFactory(rabbitConfigurer, cachingConfigurer, SingletonObjectProvider.ofNullable(customizer));
    }

    @NonNull
    public static RabbitTemplateConfigurer rabbitTemplateConfigurer(@NonNull RabbitProperties properties, @Nullable MessageConverter converter, @Nullable RabbitRetryTemplateCustomizer customizer) {
        return new RabbitTemplateConfiguration().rabbitTemplateConfigurer(properties, SingletonObjectProvider.ofNullable(converter), SingletonObjectProvider.ofNullable(customizer));
    }

    @NonNull
    public static RabbitTemplate rabbitTemplate(@NonNull RabbitTemplateConfigurer configurer, @NonNull ConnectionFactory factory, @Nullable RabbitTemplateCustomizer customizer) {
        return new RabbitTemplateConfiguration().rabbitTemplate(configurer, factory, SingletonObjectProvider.ofNullable(customizer));
    }

    @NonNull
    public static SimpleRabbitListenerContainerFactoryConfigurer simpleRabbitListenerContainerFactoryConfigurer(@NonNull RabbitProperties properties, @Nullable MessageConverter converter, @Nullable MessageRecoverer recoverer, @Nullable RabbitRetryTemplateCustomizer retryCustomizer) {
        RabbitAnnotationDrivenConfiguration configuration = new RabbitAnnotationDrivenConfiguration(SingletonObjectProvider.ofNullable(converter), SingletonObjectProvider.ofNullable(recoverer), SingletonObjectProvider.ofNullable(retryCustomizer), properties);
        return configuration.simpleRabbitListenerContainerFactoryConfigurer();
    }

    @NonNull
    public static SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(@NonNull RabbitProperties properties, @Nullable MessageConverter converter, @Nullable MessageRecoverer recoverer, @Nullable RabbitRetryTemplateCustomizer retryCustomizer, @NonNull ConnectionFactory factory, @Nullable ContainerCustomizer<SimpleMessageListenerContainer> containerCustomizer) {
        RabbitAnnotationDrivenConfiguration configuration = new RabbitAnnotationDrivenConfiguration(SingletonObjectProvider.ofNullable(converter), SingletonObjectProvider.ofNullable(recoverer), SingletonObjectProvider.ofNullable(retryCustomizer), properties);
        SimpleRabbitListenerContainerFactoryConfigurer configurer = configuration.simpleRabbitListenerContainerFactoryConfigurer();
        return configuration.simpleRabbitListenerContainerFactory(configurer, factory, SingletonObjectProvider.ofNullable(containerCustomizer));
    }

    @NonNull
    public static DirectRabbitListenerContainerFactoryConfigurer directRabbitListenerContainerFactoryConfigurer(@NonNull RabbitProperties properties, @Nullable MessageConverter converter, @Nullable MessageRecoverer recoverer, @Nullable RabbitRetryTemplateCustomizer customizer) {
        RabbitAnnotationDrivenConfiguration configuration = new RabbitAnnotationDrivenConfiguration(SingletonObjectProvider.ofNullable(converter), SingletonObjectProvider.ofNullable(recoverer), SingletonObjectProvider.ofNullable(customizer), properties);
        return configuration.directRabbitListenerContainerFactoryConfigurer();
    }

    @NonNull
    public static DirectRabbitListenerContainerFactory directRabbitListenerContainerFactory(@NonNull RabbitProperties properties, @Nullable MessageConverter converter, @Nullable MessageRecoverer recoverer, @Nullable RabbitRetryTemplateCustomizer retryCustomizer, @NonNull ConnectionFactory factory, @Nullable ContainerCustomizer<DirectMessageListenerContainer> containerCustomizer) {
        RabbitAnnotationDrivenConfiguration configuration = new RabbitAnnotationDrivenConfiguration(SingletonObjectProvider.ofNullable(converter), SingletonObjectProvider.ofNullable(recoverer), SingletonObjectProvider.ofNullable(retryCustomizer), properties);
        DirectRabbitListenerContainerFactoryConfigurer configurer = configuration.directRabbitListenerContainerFactoryConfigurer();
        return configuration.directRabbitListenerContainerFactory(configurer, factory, SingletonObjectProvider.ofNullable(containerCustomizer));
    }
}
