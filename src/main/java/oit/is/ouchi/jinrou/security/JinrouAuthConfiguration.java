package oit.is.ouchi.jinrou.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class JinrouAuthConfiguration {

  /**
   * 認証処理に関する設定（誰がどのようなロールでログインできるか）
   *
   * @return
   */
  @Bean
  public InMemoryUserDetailsManager userDetailsService() {

    UserBuilder users = User.builder();

    UserDetails user1 = users
        .username("user1")
        .password("$2y$10$86DzBg8hq6Ml4/v9yi6d7OIofNZW5tzOLSAXivVgkfkM7obVddvxe")
        .roles("ADMIN")
        .build();
    UserDetails user2 = users
        .username("user2")
        .password("$2y$10$iqpjkTb1y68osRlQyCEO0.wC.//7u0X5Dknit1UlxkEmHzbYC8unm")
        .roles("USER")
        .build();
    UserDetails user3 = users
        .username("user3")
        .password("$2y$10$jP9DogZEoPUeLCBbW3Dkgeqj2Vav.8ZyaAQ2rBU6X1XD6rr2ZvBgm")
        .roles("USER")
        .build();
    UserDetails user4 = users
        .username("user4")
        .password("$2y$10$NSVV834mL49wuMi9U9jruOGIMkZyVusmFrXpVbjHCFDzoL34/fAy.")
        .roles("USER")
        .build();
    UserDetails user5 = users
        .username("user5")
        .password("$2y$10$558FfrY45/XzoldSHiE21eSHMyC2BcnNzeUMcGHDzfqnkY5nJs8xa")
        .roles("USER")
        .build();
    UserDetails user6 = users
        .username("user6")
        .password("$2y$10$XKbHjR0PpHzZp9sRO2DXQuOZ6LpzDtN1ENen3ytGfX63dBmaEzPn2")
        .roles("USER")
        .build();

    return new InMemoryUserDetailsManager(user1, user2, user3, user4, user5, user6);
  }

  /**
   * 認可処理に関する設定（認証されたユーザがどこにアクセスできるか）
   *
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.formLogin();
    http.authorizeHttpRequests()
        .mvcMatchers("/jinrou").authenticated();
    http.logout().logoutSuccessUrl("/");
    http.csrf().disable();
    http.headers().frameOptions().disable();
    return http.build();
  }

  /**
   *
   * UserBuilder users = User.builder();で利用するPasswordEncoderを指定する．
   *
   * @return BCryptPasswordEncoderを返す
   */
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
