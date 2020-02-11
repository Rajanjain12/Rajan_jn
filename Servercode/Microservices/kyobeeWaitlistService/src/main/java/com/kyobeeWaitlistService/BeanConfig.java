package com.kyobeeWaitlistService;

import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  /*  @PersistenceUnit
    EntityManagerFactory entityManagerFactory;

    @Bean
    public SessionFactory getSessionFactory() {
        if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        return entityManagerFactory.unwrap(SessionFactory.class);
    }*/

}
