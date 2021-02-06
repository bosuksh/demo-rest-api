package me.doflamingo.demorestapi.config;

import me.doflamingo.demorestapi.accounts.domain.Account;
import me.doflamingo.demorestapi.accounts.domain.AccountRole;
import me.doflamingo.demorestapi.accounts.service.AccountService;
import me.doflamingo.demorestapi.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthServerConfigTest extends BaseControllerTest {

  @Autowired
  AccountService accountService;

  @Test
  @DisplayName("인증 토큰 받기")
  public void getAuthToken() throws Exception {
    //given
    String username = "doflamingo@example.com";
    String password = "password";
    Account account = Account.builder()
                      .email(username)
                      .password(password)
                      .accountRoles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                      .build();
    String clientId = "myApp";
    String clientSecret = "pass";

    accountService.saveAccount(account);
    //when
    this.mockMvc.perform(post("/oauth/token")
                           .with(httpBasic(clientId, clientSecret))
      .param("grant_type","password")
      .param("username", username)
      .param("password", password)
    )
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("access_token").exists())
    .andExpect(jsonPath("refresh_token").exists());
    //then
  }
}