package me.doflamingo.demorestapi.accounts.service;

import lombok.RequiredArgsConstructor;
import me.doflamingo.demorestapi.accounts.domain.Account;
import me.doflamingo.demorestapi.accounts.domain.AccountAdapter;
import me.doflamingo.demorestapi.accounts.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService{

  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;


  public Account saveAccount(Account account) {
    String encodedPassword = passwordEncoder.encode(account.getPassword());
    account.setPassword(encodedPassword);
    return accountRepository.save(account);
  }
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Account account = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    return new AccountAdapter(account);
  }

}
