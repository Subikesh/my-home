package com.spacey.data.service

import com.spacey.data.base.InputType
import com.spacey.data.base.ioScope
import java.lang.Exception
import java.time.DayOfWeek
import java.time.LocalDate

class ExpenseRepository(private val serviceDao: ServiceDao) {

    suspend fun getServices(): List<Service> {
        return serviceDao.getServices()
    }

    suspend fun updateExpense(serviceName: String, date: LocalDate, count: Int) {
        return ioScope {
            val service = serviceDao.getServiceRegistry(serviceName, date)!!
            serviceDao.replaceExpense(Expense(service.id, date, service.serviceAmount * count))
        }
    }

    suspend fun getServices(date: LocalDate): List<ServiceEntity> {
        return ioScope {
            val serviceRegistries = serviceDao.getServicesOn(date)
            serviceRegistries.map {
                val expense = serviceDao.getExpense(it.id, date)
                it.toServiceEntity(expense?.amount ?: it.defaultAmount)
            }
        }
    }

    suspend fun getService(date: LocalDate, service: String): ServiceEntity? {
        return ioScope {
            serviceDao.getServiceRegistry(service, date)?.let { serviceRegistry ->
                val weekDays = serviceDao.getWeekDays(serviceRegistry.id)
                serviceRegistry.toServiceEntity(weekDays = weekDays)
            }
        }
    }

    suspend fun getAllServices(date: LocalDate): List<ServiceEntity> {
        return ioScope {
            val serviceRegistries = serviceDao.getAllServiceRegistries(date)
            serviceRegistries.map { it.toServiceEntity() }
        }
    }

    suspend fun createService(service: Service): Boolean {
        return ioScope {
            try {
                serviceDao.insertService(listOf(service))
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    private suspend fun ServiceRegistry.toServiceEntity(amount: Double = defaultAmount, weekDays: List<DayOfWeek> = emptyList()): ServiceEntity {
        val service = serviceDao.getService(serviceId)
        return ServiceEntity(
            service = service.name,
            inputType = service.type,
            serviceAmount = serviceAmount,
            startDate = startDate,
            defaultAmount = amount,
            weekDays = weekDays
        )
    }

    suspend fun insertExpense(serviceEntity: ServiceEntity) {
        ioScope {
            val serviceId = serviceDao.getService(serviceEntity.service).id
            // TODO: give warning that later services will be deleted
            serviceDao.deleteLaterServices(serviceId, serviceEntity.startDate)
            val lastServiceRegistry = serviceDao.getServiceRegistry(serviceEntity.service, serviceEntity.startDate)
            if (lastServiceRegistry != null) {
                serviceDao.updateEndDate(lastServiceRegistry.id, serviceEntity.startDate)
            }
            val serviceRegId = serviceDao.insert(ServiceRegistry(serviceId, serviceEntity.serviceAmount, serviceEntity.defaultAmount, serviceEntity.startDate))
            serviceDao.insert(serviceEntity.weekDays.map { DateRecurrence(serviceRegId, it) })
        }
    }
}

data class ServiceEntity(
    val service: String,
    val inputType: InputType,
    val serviceAmount: Double,
    val startDate: LocalDate,
    val defaultAmount: Double,
    val weekDays: List<DayOfWeek>
) {
    val defaultCount: Int = if (serviceAmount == 0.0) 0 else (defaultAmount / serviceAmount).toInt()
}