package com.spacey.data.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.DayOfWeek
import java.time.LocalDate

@Dao
interface ServiceDao {

    @Query(
        "SELECT ServiceRegistry.* FROM ServiceRegistry JOIN DateRecurrence " +
                "ON ServiceRegistry.id = DateRecurrence.service_registry_id " +
            "WHERE week_day = :weekDay AND " +
            "start_date <= :date AND (end_date IS NULL OR end_date > :date)")
    suspend fun getServicesOn(date: LocalDate, weekDay: DayOfWeek = date.dayOfWeek): List<ServiceRegistry>

    @Query(
        "SELECT ServiceRegistry.* FROM " +
                "ServiceRegistry JOIN Service ON Service.id = ServiceRegistry.service_id " +
                "WHERE Service.name = :name AND " +
                "start_date <= :date AND (ServiceRegistry.end_date IS NULL OR ServiceRegistry.end_date > :date) LIMIT 1"
    )
    suspend fun getServiceRegistry(name: String, date: LocalDate): ServiceRegistry?

    @Query("SELECT DateRecurrence.week_day FROM DateRecurrence WHERE service_registry_id = :serviceRegId")
    suspend fun getWeekDays(serviceRegId: Long): List<DayOfWeek>

    @Query("SELECT * FROM ServiceRegistry " +
            "WHERE start_date <= :date AND (end_date IS NULL OR end_date > :date)")
    suspend fun getAllServiceRegistries(date: LocalDate): List<ServiceRegistry>

    @Query("SELECT * FROM Service")
    suspend fun getServices(): List<Service>

    @Query("SELECT * FROM Service WHERE name = :serviceName")
    suspend fun getService(serviceName: String): Service

    @Query("SELECT * FROM Service WHERE id = :id")
    suspend fun getService(id: Long): Service

    @Query("SELECT * FROM Expense WHERE service_registry_id = :serviceRegId AND date = :date")
    suspend fun getExpense(serviceRegId: Long, date: LocalDate): Expense?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceExpense(expense: Expense)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(serviceRegistry: ServiceRegistry): Long

    @Query("UPDATE ServiceRegistry SET end_date = :endDate WHERE id = :serviceRegId")
    suspend fun updateEndDate(serviceRegId: Long, endDate: LocalDate)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dateRecurrence: List<DateRecurrence>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(services: List<Service>)

    @Query("DELETE FROM ServiceRegistry WHERE service_id == :serviceId AND start_date >= :date")
    suspend fun deleteLaterServices(serviceId: Long, date: LocalDate)
}