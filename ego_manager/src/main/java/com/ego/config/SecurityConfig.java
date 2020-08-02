package com.ego.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //使用BCrypt算法加密
    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //form表单登录验证
        http.formLogin()
                .loginProcessingUrl("/login")  //处理登录post请求接口，无需自己实现
                .successForwardUrl("/loginSuccess")  //登录成功转发接口
                .loginPage("/");  ////未登录跳转页面,设置了authenticationentrypoint后无需设置未登录跳转

        //谁可以访问什么
        http.authorizeRequests()
                .antMatchers("/","/css/**","/js/**").permitAll() //都可以访问（静态资源）
                .anyRequest().authenticated(); //除上面的其他的所有http请求都要验证

        //关闭打开的csrf保护
        http.csrf().disable();

        // deny禁止iframe调用
        http.headers().frameOptions().disable();
    }
}
