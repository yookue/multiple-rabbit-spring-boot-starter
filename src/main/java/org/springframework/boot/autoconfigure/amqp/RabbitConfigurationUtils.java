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


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    public static RabbitConnectionFactoryBeanConfigurer rabbitConnectionFactoryBeanConfigurer(@NonNull RabbitProperties properties, @NonNull ResourceLoader loader, @Nullable CredentialsProvider credentialsProvider, @Nullable CredentialsRefreshService refreshService) {
        return new RabbitConnectionFactoryCreator().rabbitConnectionFactoryBeanConfigurer(properties, loader, SingletonObjectProvider.ofNullable(credentialsProvider), SingletonObjectProvider.ofNullable(refreshService));
    }

    @Nonnull
    public static CachingConnectionFactoryConfigurer cachingConnectionFactoryConfigurer(@NonNull RabbitProperties properties, @Nullable ConnectionNameStrategy strategy) {
        return new RabbitConnectionFactoryCreator().rabbitConnectionFactoryConfigurer(properties, SingletonObjectProvider.ofNullable(strategy));
    }

    @NonNull
    public static CachingConnectionFactory cachingConnectionFactory(@NonNull RabbitConnectionFactoryBeanConfigurer rabbitFactoryConfigurer, @NonNull CachingConnectionFactoryConfigurer cachingFactoryConfigurer, @Nullable ConnectionFactoryCustomizer customizer) throws Exception {
        return new RabbitConnectionFactoryCreator().rabbitConnectionFactory(rabbitFactoryConfigurer, cachingFactoryConfigurer, SingletonObjectProvider.ofNullable(customizer));
    }

    @NonNull
    public static RabbitTemplateConfigurer rabbitTemplateConfigurer(@NonNull RabbitProperties properties, @Nullable MessageConverter converter, @Nullable RabbitRetryTemplateCustomizer customizer) {
        return new RabbitTemplateConfiguration().rabbitTemplateConfigurer(properties, SingletonObjectProvider.ofNullable(converter), SingletonObjectProvider.ofNullable(customizer));
    }

    @NonNull
    public static RabbitTemplate rabbitTemplate(@NonNull RabbitTemplateConfigurer configurer, @NonNull ConnectionFactory factory) {
        return new RabbitTemplateConfiguration().rabbitTemplate(configurer, factory);
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
