package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.services.UserService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    private final SuccessUserHandler successUserHandler;
//
//    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
//        this.successUserHandler = successUserHandler;
//    }

//    private SuccessUserHandler successUserHandler;
    private UserService userService;

    @Autowired
    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // Нурсултан предложил добавить это: .csrf().disable()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .formLogin()
//                .successHandler(successUserHandler)  //не понимаю смысл пока
                .and()
                .logout().logoutSuccessUrl("/");
                //.authenticated()

                // если тут есть другие части приложения - их тоже прописываем
                //.antMatchers("/admin/**").hasAnyRole("ADMIN", "DEVELOPER")
                // .antMatchers("/user/**").authenticated()

                //.loginProcessingUrl("/myLoginProcess") - перенаправляем данные с поля login
                //.successForwardUrl("/") - страница на которую отправляем всех успешно зашедших пользователей
                //.successHandler - преднастройки для зашедшего человека
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }
// То как это сделано у Нурсултана
//    @Autowired
//    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService)
//                .passwordEncoder(bcryptPasswordEncoder());
//    }

    //jdbcAuthentication
//    @Bean
//    public JdbcUserDetailsManager users(DataSource dataSource){
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{bcrypt}$2a$12$ROmf00Z.BN0dLvomQT/aLOus23Sm4.Enwo34UcPr9OL.13yx3KSUO")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$12$ROmf00Z.BN0dLvomQT/aLOus23Sm4.Enwo34UcPr9OL.13yx3KSUO")
//                .roles("ADMIN", "USER")
//                .build();
//        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//        //проверяем есть ли уже user'ы с такими именами в БД
//        if (users.userExists(user.getUsername())){
//            users.deleteUser(user.getUsername()); //если есть - удаляем
//        }
//        if (users.userExists(admin.getUsername())){
//            users.deleteUser(admin.getUsername());
//        }
//        users.createUser(user);
//        users.createUser(admin);
//        //мы создаем в БД юзеров по умолчанию при запуске приложения
//        return users;
//    }
    // аутентификация inMemory (без использования бд)
//    @Bean
//    public UserDetailsService users(){
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{bcrypt}$2a$12$ROmf00Z.BN0dLvomQT/aLOus23Sm4.Enwo34UcPr9OL.13yx3KSUO")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$12$ROmf00Z.BN0dLvomQT/aLOus23Sm4.Enwo34UcPr9OL.13yx3KSUO")
//                .roles("ADMIN", "USER")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }
}