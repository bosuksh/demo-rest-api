package me.doflamingo.demorestapi.accounts.service;

import lombok.RequiredArgsConstructor;
import me.doflamingo.demorestapi.accounts.domain.Account;
import me.doflamingo.demorestapi.accounts.domain.AccountRole;
import me.doflamingo.demorestapi.accounts.repository.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

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
    return new User(account.getEmail(), account.getPassword(), authorities(account.getAccountRoles()));
  }

  private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> accountRoles) {
    return accountRoles.stream()
             .map(r -> new SimpleGrantedAuthority("ROLE_"+r.name()))
             .collect(Collectors.toSet());
  }
}
