package me.doflamingo.demorestapi.config;

import me.doflamingo.demorestapi.accounts.domain.Account;
import me.doflamingo.demorestapi.accounts.domain.AccountRole;
import me.doflamingo.demorestapi.accounts.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner appRunner() {
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) {
                Account admin= Account.builder()
                                    .email(appProperties.getAdminEmail())
                                    .password(appProperties.getAdminPassword())
                                    .accountRoles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                                    .build();
                Account user= Account.builder()
                                 .email(appProperties.getUserEmail())
                                 .password(appProperties.getUserPassword())
                                 .accountRoles(Set.of(AccountRole.USER))
                                 .build();
                accountService.saveAccount(admin);
                accountService.saveAccount(user);
            }
        };
    }
}
