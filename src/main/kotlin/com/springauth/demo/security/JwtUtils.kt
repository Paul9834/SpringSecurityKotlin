package com.springauth.demo.security

import com.springauth.demo.user_details.UserDetailsImpl
import io.jsonwebtoken.*

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtUtils   {

    private val logger: Logger = LoggerFactory.getLogger(JwtUtils::class.java)


    @Value("\${bezkoder.app.jwtSecret}")
    private val jwtSecret: String? = null

    @Value("\${bezkoder.app.jwtExpirationMs}")
    private val jwtExpirationMs = 0

    public fun generateJwtToken (authentication: Authentication) : String{

        val userPrincipal:UserDetailsImpl = authentication.principal as UserDetailsImpl

        val str = Jwts.builder()
                .setSubject((userPrincipal.username))
                .setIssuedAt(Date())
                .setExpiration(Date(Date().time + jwtExpirationMs))
                .signWith(SignatureAlgorithm.ES512, jwtSecret)
                .compact()

        return str
    }

    fun getUserNameFromJwtToken (token:String) : String{
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
    }


    fun validJwtToken(token:String) : Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
            return true
        } catch (e: SignatureException) {
            logger.error("Invalid JWT signature: {}", e.message)
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e)
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.message)
        } catch (e:UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
        }
        return false
    }


}