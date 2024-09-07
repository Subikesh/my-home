package com.spacey.myhome.domain

import java.time.DayOfWeek
import java.time.LocalDate

data class User(val userName: String, val password: String)

data class Deliverer(val user: User)

data class Service(val name: String)

data class ServiceJob(val deliverer: Deliverer, val service: Service)

data class ServiceRegistry(val service: ServiceJob, val subscriber: User)

data class Subscription(val serviceRegistry: ServiceRegistry, val startDate: LocalDate, val days: List<DayOfWeek>, val default: Double)

data class Modification(val subscription: Subscription, val amount: Double)