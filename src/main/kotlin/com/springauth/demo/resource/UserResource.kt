package com.springauth.demo.resource

import com.springauth.demo.request.LoginRequest
import com.springauth.demo.request.SignUpRequest
import com.springauth.demo.security.JwtAuthenticationResponse
import com.springauth.demo.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/v1/api")
class UserResource (private val userService: UserService){

    @PostMapping ("/login")
    fun loginUser(@RequestBody loginRequest: LoginRequest) : ResponseEntity<JwtAuthenticationResponse> {
        return userService.loginUser(loginRequest)
    }

    @PostMapping ("/register")
    fun createUser(@RequestBody signUpRequest: SignUpRequest) : ResponseEntity<*>{
       return userService.signUpUser(signUpRequest)
    }

}