package com.springauth.demo.user_details

import org.springframework.security.core.userdetails.UserDetails

interface UserDetailsService {
    fun loadByUserName(username:String) : UserDetails
}