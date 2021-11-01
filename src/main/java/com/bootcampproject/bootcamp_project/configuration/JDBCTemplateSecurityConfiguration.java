package com.bootcampproject.bootcamp_project.configuration;

import com.bootcampproject.bootcamp_project.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

//@Configuration
//@EnableWebSecurity
public class JDBCTemplateSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        "select email,password,true as is_active from user where email=?")
                .authoritiesByUsernameQuery(
                            "select email,authority from role r "
                                    + "inner join user_role ur on ur.roleid=r.id inner join user u on u.id=ur.user_id where u.email=?");
    }

    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin").hasRole(RoleEnum.ADMIN.name())
                .antMatchers("/seller").hasAnyRole(RoleEnum.SELLER.name(), RoleEnum.ADMIN.name())
                .antMatchers("/customer").hasAnyRole(RoleEnum.CUSTOMER.name(), RoleEnum.ADMIN.name())
                .antMatchers("/").permitAll()
                .antMatchers("/registration").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .permitAll();
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

}