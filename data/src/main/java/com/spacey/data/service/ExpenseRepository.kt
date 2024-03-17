package com.spacey.data.service

import com.spacey.data.base.InputType
import com.spacey.data.base.ioScope
import java.time.DayOfWeek
import java.time.LocalDate

class ExpenseRepository(private val serviceDao: ServiceDao) {

    suspend fun getServices(): List<Service> {
        return serviceDao.getServices()
    }

    suspend fun getServices(date: LocalDate): List<ServiceEntity> {
        return ioScope {
            val serviceRegistries = serviceDao.getServicesOn(date)
            serviceRegistries.map {
                it.toServiceEntity()
            }
        }
    }

    suspend fun getAllServices(date: LocalDate): List<ServiceEntity> {
        return ioScope {
            val serviceRegistries = serviceDao.getAllServiceRegistries(date)
            serviceRegistries.map { it.toServiceEntity() }
        }
    }

    private suspend fun ServiceRegistry.toServiceEntity(weekDays: List<DayOfWeek> = emptyList()): ServiceEntity {
        val service = serviceDao.getService(serviceId)
        return ServiceEntity(
            service = service.name,
            inputType = service.type,
            serviceAmount = amount,
            startDate = startDate,
            defaultAmount = defaultAmount,
            weekDays = weekDays
        )
    }

    suspend fun insertExpense(serviceEntity: ServiceEntity) {
        ioScope {
            val serviceId = serviceDao.getService(serviceEntity.service).id
            // TODO: give warning that later services will be deleted
            serviceDao.deleteLaterServices(serviceEntity.startDate)
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
)