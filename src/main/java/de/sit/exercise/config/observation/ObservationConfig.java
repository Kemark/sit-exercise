package de.sit.exercise.config.observation;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.ServerRequestObservationContext;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.ObservationPredicate;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.DefaultNewSpanParser;
import io.micrometer.tracing.annotation.ImperativeMethodInvocationProcessor;
import io.micrometer.tracing.annotation.MethodInvocationProcessor;
import io.micrometer.tracing.annotation.NewSpanParser;
import io.micrometer.tracing.annotation.SpanAspect;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.extension.trace.propagation.JaegerPropagator;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import jakarta.servlet.http.HttpServletRequest;
import net.ttddyy.observation.tracing.ConnectionContext;

/**
 * Enables @Observed support
 */
@Configuration
public class ObservationConfig {

    @Value("${management.otlp.tracing.endpoint}")
    private String endpoint;

    @Value("${spring.profiles.active:unknown}")
    private String activeProfile;

    /**
     *
     */
    @Bean
    NewSpanParser newSpanParser() {
        return new DefaultNewSpanParser();
    }

    /**
     *
     */
    @Bean
    MethodInvocationProcessor methodInvocationProcessor(
            final NewSpanParser newSpanParser, final Tracer tracer, final BeanFactory beanFactory) {
        return new ImperativeMethodInvocationProcessor(
                newSpanParser, tracer, beanFactory::getBean, beanFactory::getBean);
    }

    /**
     *
     */
    @Bean
    SpanAspect spanAspect(final MethodInvocationProcessor methodInvocationProcessor) {
        return new SpanAspect(methodInvocationProcessor);
    }

    /**
     *
     */
    @Bean
    public TextMapPropagator jaegerPropagator() {
        return JaegerPropagator.getInstance();
    }

    /**
     *
     */
    @Bean
    ObservedAspect observedAspect(final ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }

    /**
     *
     */
    @Bean
    public SpanExporter otlpExporter() {
        return OtlpGrpcSpanExporter.builder().setEndpoint(this.endpoint).build();
    }

    /**
     *
     */
    @Bean
    ObservationPredicate ignoreObservations() {
        return (name, context) -> {
            if (name.startsWith("spring.security")) {
                return false;
            }

            if (context instanceof ServerRequestObservationContext serverRequestObservationContext) {
                HttpServletRequest carrier = serverRequestObservationContext.getCarrier();
                return !carrier.getServletPath().startsWith("/actuator");
            }

            context.addLowCardinalityKeyValue(KeyValue.of("env", activeProfile));
            return !(context instanceof ConnectionContext);
        };
    }
}
