package io.graversen.replicate.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ReplicateConfiguration.class)
public class ReplicateAutoConfiguration {

}
