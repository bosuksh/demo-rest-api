package me.doflamingo.demorestapi.accounts.service;

import me.doflamingo.demorestapi.accounts.domain.Account;
import me.doflamingo.demorestapi.accounts.domain.AccountRole;
import me.doflamingo.demorestapi.accounts.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {
  @Autowired
  AccountService accountService;
  @Autowired
  AccountRepository accountRepository;

  @Test
  @DisplayName("정상 인증 과정")
  public void findByEmail() {
    //given
    String password = "password";
    Account account = Account.builder()
                        .email("doflamingo@email.com")
                        .password(password)
                        .accountRoles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();
    accountRepository.save(account);
    //when
    UserDetails userDetails = accountService.loadUserByUsername(account.getEmail());
    //then
    assertThat(userDetails.getPassword()).isEqualTo(password);
  }
}