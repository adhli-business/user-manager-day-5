package com.example.user_manager.data.models

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String? = null,
    val username: String? = null,
    val password: String? = null,
    val birthDate: String? = null,
    val image: String? = null,
    val bloodGroup: String? = null,
    val height: Double? = null,
    val weight: Double? = null,
    val eyeColor: String? = null,
    val hair: Hair? = null,
    val domain: String? = null,
    val ip: String? = null,
    val address: Address? = null,
    val macAddress: String? = null,
    val university: String? = null,
    val bank: Bank? = null,
    val company: Company? = null,
    val ein: String? = null,
    val ssn: String? = null,
    val userAgent: String? = null,
    val crypto: Crypto? = null,
    val role: String? = null,
    val gender: String? = null,
    val age: Int? = null
)

data class Hair(
    val color: String,
    val type: String
)

data class Address(
    val address: String,
    val city: String,
    val state: String,
    val stateCode: String,
    val postalCode: String,
    val coordinates: Coordinates,
    val country: String
)

data class Coordinates(
    val lat: Double,
    val lng: Double
)

data class Bank(
    val cardExpire: String,
    val cardNumber: String,
    val cardType: String,
    val currency: String,
    val iban: String
)

data class Company(
    val department: String,
    val name: String,
    val title: String,
    val address: Address
)

data class Crypto(
    val coin: String,
    val wallet: String,
    val network: String
)