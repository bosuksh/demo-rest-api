package me.doflamingo.demorestapi.accounts.service;

import me.doflamingo.demorestapi.accounts.domain.Account;
import me.doflamingo.demorestapi.accounts.domain.AccountRole;
import me.doflamingo.demorestapi.accounts.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

  @Autowired
  AccountService accountService;
  @Autowired
  AccountRepository accountRepository;
  @Autowired
  PasswordEncoder passwordEncoder;

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
    assertThat(passwordEncoder.matches(password,userDetails.getPassword())).isTrue();
  }

  @Test
  @DisplayName("인증 실패 과정")
  public void findByEmailFail() {
    //given
    String randomName = "random@exmaple.com";
    //when
    UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class, () -> accountService.loadUserByUsername(randomName));
    //then
    assertThat(usernameNotFoundException).hasMessageContaining(randomName);
  }
}