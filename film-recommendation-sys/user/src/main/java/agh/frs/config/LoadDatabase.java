package agh.frs.config;


import agh.frs.repositiory.RoleRepository;
import agh.frs.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    @Bean
    CommandLineRunner initDatabase(RoleRepository repositiory) {
        return args -> {
            log.info("Preloading " + repositiory.save(new Role("USER")));
        };
    }
}
