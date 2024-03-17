package com.spacey.data.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.DayOfWeek
import java.time.LocalDate

@Dao
abstract class ServiceDao {

    @Query(
        "SELECT ServiceRegistry.* FROM ServiceRegistry JOIN DateRecurrence " +
                "ON ServiceRegistry.id = DateRecurrence.service_registry_id " +
            "WHERE week_day = :weekDay AND " +
            "start_date <= :date AND (end_date IS NULL OR end_date > :date)")
    abstract fun getServicesOn(date: LocalDate, weekDay: DayOfWeek = date.dayOfWeek): List<ServiceRegistry>

    @Query(
        "SELECT ServiceRegistry.* FROM " +
                "ServiceRegistry JOIN Service ON Service.id = ServiceRegistry.service_id " +
                "WHERE Service.name = :name AND " +
                "start_date <= :date AND (ServiceRegistry.end_date IS NULL OR ServiceRegistry.end_date > :date) LIMIT 1"
    )
    abstract fun getServiceRegistry(name: String, date: LocalDate): ServiceRegistry?

    @Query("SELECT * FROM Service WHERE name = :serviceName")
    abstract fun getService(serviceName: String): Service

    @Query("SELECT * FROM Service WHERE id = :id")
    abstract fun getService(id: Long): Service

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(serviceRegistry: ServiceRegistry): Long

    @Query("UPDATE ServiceRegistry SET end_date = :endDate WHERE id = :serviceRegId")
    abstract suspend fun updateEndDate(serviceRegId: Long, endDate: LocalDate)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(dateRecurrence: List<DateRecurrence>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertService(services: List<Service>)

    @Query("DELETE FROM ServiceRegistry WHERE start_date >= :date")
    abstract suspend fun deleteLaterServices(date: LocalDate)
}