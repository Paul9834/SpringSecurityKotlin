package com.springauth.demo.security

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

class WebSecurityConfig : WebSecurityConfigurerAdapter()  {

    override fun configure(http: HttpSecurity?) {
        super.configure(http)

        http.addFilter()

    }

}