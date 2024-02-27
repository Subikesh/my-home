package com.spacey.data.service

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import java.time.DayOfWeek
import java.time.LocalDate

@Dao
abstract class ExpenseDao {

    fun getExpenses(date: LocalDate): List<NewExpenseEntity> {
        val expenses = getDefaultExpense(date).associateBy { it.serviceRegistryId }.toMutableMap()
        expenses.putAll(getDateExpense(date).associateBy { it.serviceRegistryId })
        return expenses.filter { (_, expense) ->
            val dateRecurrence = getDateRecurrence(expense.dateRecurrenceId)
            date.dayOfWeek in dateRecurrence.weekDays
        }.map { it.value.toEntity() }
    }

    suspend fun insert(newExpenseEntity: NewExpenseEntity, isDaily: Boolean): Boolean {
        return try {
            val serviceRegId = getServiceRegistry(newExpenseEntity.service.name)?.id ?: let {
                // TODO: Remove serviceInsertion after inserting default entries for service table
                val serviceId = insert(newExpenseEntity.service)
                val serviceRegistry = ServiceRegistry(serviceId, newExpenseEntity.serviceAmount)
                insert(serviceRegistry)
            }

            val dateRecurrenceList = getDateRecurrence(newExpenseEntity.startDate, newExpenseEntity.weekDays)
            val dateRecurrenceId = if (dateRecurrenceList.isEmpty()) {
                val dateRecurrence = DateRecurrence(newExpenseEntity.startDate, newExpenseEntity.weekDays)
                insert(dateRecurrence)
            } else{
                dateRecurrenceList.first().id
            }
            // TODO: isDaily is only for false. Handle today's update here
            insert(Expense(serviceRegId, dateRecurrenceId, newExpenseEntity.amount, isDaily))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun Expense.toEntity(): NewExpenseEntity {
        val serviceRegistry = getServiceRegistry(serviceRegistryId)
        val service = getService(serviceRegistry.serviceId)
        val recurrence = getDateRecurrence(this.dateRecurrenceId)
        return NewExpenseEntity(
            startDate = recurrence.startDate,
            service = service,
            serviceAmount = serviceRegistry.amount,
            amount = this.amount,
            weekDays = recurrence.weekDays,
        )
    }

    @Query(
        "SELECT Expense.* FROM Expense " +
                "JOIN DateRecurrence ON Expense.date_recurrence_id = DateRecurrence.id " +
                "WHERE Expense.is_daily = 0 AND :date >= start_date"
    )
    protected abstract fun getDefaultExpense(date: LocalDate): List<Expense>

    @Query(
        "SELECT Expense.* FROM Expense " +
                "JOIN DateRecurrence ON Expense.date_recurrence_id = DateRecurrence.id " +
                "WHERE start_date = :date AND Expense.is_daily = 1"
    )
    abstract fun getDateExpense(date: LocalDate): List<Expense>

    @Query("SELECT * FROM DateRecurrence WHERE id = :recurrenceId")
    protected abstract fun getDateRecurrence(recurrenceId: Long): DateRecurrence

    @Query("SELECT * FROM DateRecurrence WHERE start_date = :date AND week_days = :weekDays")
    protected abstract fun getDateRecurrence(date: LocalDate, weekDays: List<DayOfWeek>): List<DateRecurrence>

    @Query("SELECT ServiceRegistry.* FROM " +
            "ServiceRegistry JOIN Service ON Service.id = ServiceRegistry.service_id " +
            "WHERE Service.name = :name LIMIT 1")
    protected abstract fun getServiceRegistry(name: String): ServiceRegistry?

    @Query("SELECT * FROM ServiceRegistry WHERE id = :serviceRegistryId")
    protected abstract fun getServiceRegistry(serviceRegistryId: Long): ServiceRegistry

    @Query("SELECT * FROM Service WHERE id = :serviceId")
    protected abstract fun getService(serviceId: Long): Service

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insert(serviceRegistry: ServiceRegistry): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(expense: Expense): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insert(dateRecurrence: DateRecurrence): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(service: Service): Long

    @Query("DELETE FROM Expense WHERE id = :id")
    abstract suspend fun delete(id: Long)
}

data class ExpenseEntity(
    @Relation(parentColumn = ExpenseCol.SERVICE_REGISTRY_ID, entityColumn = "id") val service: Service,
    @ColumnInfo(ExpenseCol.AMOUNT) val amount: Double,
    @ColumnInfo(DateRecurrenceCol.START_DATE) val startDate: LocalDate,
    @ColumnInfo(DateRecurrenceCol.UPDATED_TIME) val updatedTime: Long = System.currentTimeMillis(),
    @ColumnInfo(ExpenseCol.SERVICE_REGISTRY_ID) private val serviceId: Long = 0,
    val id: Long = -1L
)

