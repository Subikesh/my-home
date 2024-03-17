package com.spacey.data.service

import com.spacey.data.base.InputType
import com.spacey.data.base.ioScope
import java.time.DayOfWeek
import java.time.LocalDate

class ExpenseRepository(private val serviceDao: ServiceDao) {

    suspend fun getServices(date: LocalDate): List<ServiceEntity> {
        return ioScope {
            val serviceRegistries = serviceDao.getServicesOn(date)
            serviceRegistries.map {
                val service = serviceDao.getService(it.serviceId)
                ServiceEntity(
                    service = service.name,
                    inputType = service.type,
                    serviceAmount = it.amount,
                    startDate = it.startDate,
                    defaultAmount = it.defaultAmount,
                    weekDays = emptyList() // TODO: Create a separate entity to return and don't send unnecessary fields there?
                )
            }
        }
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