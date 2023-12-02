package com.spacey.data.service

import com.spacey.data.base.ioScope

class ServiceRepository(private val serviceDao: ServiceDao) {
    suspend fun addService(service: Service) {
        ioScope {
            serviceDao.insert(service)
        }
    }
}