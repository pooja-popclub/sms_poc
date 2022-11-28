package com.avdt.retorfit

data class ApiResponse(
    val is_success: Boolean,
    val message: String,
    val access_token: String,
    val data:DTO,
    val user:User
)
data class DTO(
    val GR: String,
    val UP: String,
    val ENT: String,
    val FO: String,
    val DN: String,
    val TR: String,
    val EC: String,
    val PHR: String
)
data class User(
    val phone_number: String
)