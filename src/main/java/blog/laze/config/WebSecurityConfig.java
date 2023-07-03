package blog.laze.config;

import blog.laze.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailService userService;

    @Bean
    public WebSecurityCustomizer configure(){
        return (web -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.authorizeRequests()
                .requestMatchers("/login", "/signup", "/user").permitAll() // requestMatchers - 특정 요청과 일치하는 url에 대한 액세스 설정 , permitAll() - 누구나 접근 가능하게 설정
                .anyRequest().authenticated() // anyRequest - 위에서 설정한 url 이외의 요청에 대해 설정, authenticated - 별도의 인가는 필요하지 않지만 인증이 접근할 수 있음
                .and()
                .formLogin()
                .loginPage("/login") // 로그인 페이지 경로를 설정
                .defaultSuccessUrl("/articles") // 로그인이 완료됐을 때 이동할 경로 설정
                .and()
                .logout()// 로그아웃이 완료됐을 때 이동할 경로 설정
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true) // 로그아웃 이후에 세션을 전체 삭제할지 여부 설정
                .and()
                .csrf().disable()
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager (HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception{
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)// 사용자 정보를 가져올 서비스 설정, 이때 설정할 서비스는 반드시 UserDetailsService 를 상속받은 클래스여야 함
                .passwordEncoder(bCryptPasswordEncoder) // 비밀번호를 암호화하기 위한 인코더 설정
                .and()
                .build();

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
