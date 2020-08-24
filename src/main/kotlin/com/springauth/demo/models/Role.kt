package com.springauth.demo.models

import javax.persistence.*


@Entity
data class Role (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id:Int = 0,

        @Enumerated(EnumType.STRING)
        @Column(length = 20)
        val name: ERole
)