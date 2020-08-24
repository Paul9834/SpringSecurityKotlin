package com.springauth.demo.security

data class JwtAuthenticationResponse(
        val token:String,
        val id: Long,
        val username:String,
        val email: String,
        val roles: MutableList<String> = mutableListOf()
)