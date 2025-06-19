package com.example.ticketing.model

data class Queue(
    val id: Long = 0,
    val nik: String,
    val name: String,
    val poly: String,
    val need: String,
    val queueNumber: String,
    var status: String,
    val createdAt: Long
) 