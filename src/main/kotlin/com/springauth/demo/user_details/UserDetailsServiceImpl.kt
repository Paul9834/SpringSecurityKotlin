package com.springauth.demo.user_details

import com.springauth.demo.models.User
import com.springauth.demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Transactional
    override fun loadByUserName(username: String): UserDetails {
        val user: User = userRepository
                .findByUsername(username)
                .orElseThrow {
            UsernameNotFoundException("User Not Found with username: $username")
        }

        return UserDetailsImpl.build(user)
    }
}