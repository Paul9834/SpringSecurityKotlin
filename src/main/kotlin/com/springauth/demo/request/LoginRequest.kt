package com.springauth.demo.request

import javax.validation.constraints.NotBlank

data class LoginRequest (
        @NotBlank
        val username:String,
        @NotBlank
        val password:String
)