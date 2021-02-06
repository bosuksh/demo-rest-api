package me.doflamingo.demorestapi.config;

import lombok.RequiredArgsConstructor;
import me.doflamingo.demorestapi.accounts.service.AccountService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;
  private final AccountService accountService;
  private final TokenStore tokenStore;
  private final AuthenticationManager authenticationManager;

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) {
    security.passwordEncoder(passwordEncoder);
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
      .withClient("myApp")
      .authorizedGrantTypes("password","refresh_token")
      .scopes("read","write")
      .secret(passwordEncoder.encode("pass"))
      .accessTokenValiditySeconds(10 * 60)
      .refreshTokenValiditySeconds(60 * 60);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints.userDetailsService(accountService)
      .authenticationManager(authenticationManager)
      .tokenStore(tokenStore);
  }
}
