package com.springauth.demo.service

import com.springauth.demo.TestException
import com.springauth.demo.models.ERole
import com.springauth.demo.models.Role
import com.springauth.demo.models.User
import com.springauth.demo.repository.RoleRepository
import com.springauth.demo.repository.UserRepository
import com.springauth.demo.request.LoginRequest
import com.springauth.demo.request.SignUpRequest
import com.springauth.demo.response.GenericMessageResponse
import com.springauth.demo.security.JwtAuthenticationResponse
import com.springauth.demo.security.JwtUtils
import com.springauth.demo.user_details.UserDetailsImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*
import java.util.stream.Collectors


@Service
class UserService(private val userRepository: UserRepository,
                  private val roleRepository: RoleRepository) {

@Autowired
private lateinit var authenticationManager: AuthenticationManager

@Autowired
private lateinit var encoder: PasswordEncoder

@Autowired
private lateinit var jwtUtils: JwtUtils


fun loginUser(loginRequest: LoginRequest): ResponseEntity<JwtAuthenticationResponse> {

    val authentication: Authentication = authenticationManager
            .authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))

     val securityContext: SecurityContext = SecurityContextHolder.getContext()

     securityContext.authentication = authentication /////////

    val jwt = jwtUtils.generateJwtToken(authentication)


    val userDetails: UserDetailsImpl = authentication.principal as UserDetailsImpl

    val roles = userDetails.authorities.stream()
            .map { item: GrantedAuthority? -> item!!.authority }
            .collect(Collectors.toList())

    return ResponseEntity.ok(JwtAuthenticationResponse(jwt, userDetails.id, userDetails.username, userDetails.email, roles))

}

    fun signUpUser(signUpRequest: SignUpRequest) : ResponseEntity<*> {

        if (userRepository.existsByUsername(signUpRequest.username)) {
            return ResponseEntity.badRequest().body(GenericMessageResponse("El usuario ya existe"))
        }

        if (userRepository.existsByEmail(signUpRequest.email)) {
            return ResponseEntity.badRequest().body(GenericMessageResponse("El usuario ya existe"))
        }

        val user = User(signUpRequest.username, signUpRequest.email, signUpRequest.password)

        val userRole: Role = roleRepository.findByName(ERole.ROLE_USER).orElseThrow{ TestException("Role not set") }

        user.roles = Collections.singleton(userRole)

        val result = userRepository.save(user)

        val location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.username).toUri()

        return ResponseEntity.created(location).body("User registered successfully")
    }



}
