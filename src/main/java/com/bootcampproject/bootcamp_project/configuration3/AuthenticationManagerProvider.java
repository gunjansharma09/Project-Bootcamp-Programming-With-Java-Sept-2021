package com.bootcampproject.bootcamp_project.configuration3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
public class AuthenticationManagerProvider extends WebSecurityConfigurerAdapter {
    @Autowired
    DataSource dataSource;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "select email,password,is_active from user where email=?")
                .authoritiesByUsernameQuery(
                        "select email,authority from role r "
                                + "inner join user_role ur on ur.roleid=r.id inner join user u on u.id=ur.user_id where u.email=?");

    }

}
