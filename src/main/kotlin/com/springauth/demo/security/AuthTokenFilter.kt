package com.springauth.demo.security


import com.springauth.demo.user_details.UserDetailsServiceImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthTokenFilter : OncePerRequestFilter(){

    @Autowired
    private lateinit var userDetailsServiceImpl:UserDetailsServiceImpl

    @Autowired
    private val jwtUtils: JwtUtils? = null


    private  val logger2: Logger = LoggerFactory.getLogger(AuthTokenFilter::class.java)


    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {

        try {
            val jwt: String? = parseJwt(request)

            if (jwtUtils != null) {
                if (jwt != null && jwtUtils.validJwtToken(jwt)) {

                    val username = jwtUtils.getUserNameFromJwtToken(jwt)
                    val userDetails:UserDetails = userDetailsServiceImpl.loadByUserName(username)

                    val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

                    authentication.details = (WebAuthenticationDetailsSource().buildDetails(request))

                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        } catch (e: Exception) {
            logger2.error("Cannot set user authentication: {}", e.message)
        }

        filterChain.doFilter(request, response)


    }

    private fun parseJwt(request: HttpServletRequest) : String? {
        val headerAuth: String = request.getHeader("Authorization")
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length)
        }
        return null
    }
}