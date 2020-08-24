package com.springauth.demo.models

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(	name = "users",
        uniqueConstraints = [
            UniqueConstraint(columnNames = arrayOf("username")),
            UniqueConstraint(columnNames = arrayOf("email"))])
data class User (

    @NotBlank
    @Size(max = 20)
    var username:String,

    @NotBlank
    @Email
    @Size(max = 50)
    val email:String,

    @NotBlank
    @Size(max = 120)
    val password:String,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = [JoinColumn(name = "user_id")],
            inverseJoinColumns = [JoinColumn(name = "role_id")])
    var roles: MutableSet<Role> = hashSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long = 0


)