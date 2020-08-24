package com.springauth.demo.security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AuthEntryPointJwt : AuthenticationEntryPoint {

    private val logger: Logger = LoggerFactory.getLogger(AuthEntryPointJwt::class.java)

    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, authException: AuthenticationException?) {
        logger.error("Unauthorized error: {}", authException?.message)
        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized")
    }
}