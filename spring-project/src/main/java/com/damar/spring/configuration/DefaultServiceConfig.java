package com.damar.spring.configuration;

import com.damar.spring.model.Player;
import com.damar.spring.repository.entity.PlayerEntity;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultServiceConfig {

    @Bean
    public DozerBeanMapper initializeMapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(createPlayerMappingBuilder());
        return mapper;
    }

    private BeanMappingBuilder createPlayerMappingBuilder() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(PlayerEntity.class, Player.class);
            }
        };
    }

}
