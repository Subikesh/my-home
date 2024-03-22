package com.spacey.data.service

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.spacey.data.base.InputType
import java.time.DayOfWeek
import java.time.LocalDate

@Entity(indices = [Index(ServiceCol.NAME, unique = true)])
data class Service(
    @ColumnInfo(ServiceCol.NAME) val name: String,
    @ColumnInfo(ServiceCol.TYPE) val type: InputType,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Entity(
    primaryKeys = [DateRecurrenceCol.SERVICE_REGISTRY_ID, DateRecurrenceCol.WEEK_DAY],
    foreignKeys = [ForeignKey(ServiceRegistry::class, ["id"], [ExpenseCol.SERVICE_REGISTRY_ID], onDelete = ForeignKey.CASCADE)]
)
data class DateRecurrence(
    @ColumnInfo(DateRecurrenceCol.SERVICE_REGISTRY_ID) val serviceRegId: Long,
    @ColumnInfo(DateRecurrenceCol.WEEK_DAY) val weekDay: DayOfWeek
)

@Entity(
    primaryKeys = [ExpenseCol.SERVICE_REGISTRY_ID, ExpenseCol.DATE],
    foreignKeys = [ForeignKey(ServiceRegistry::class, ["id"], [ExpenseCol.SERVICE_REGISTRY_ID])]
)
data class Expense(
    @ColumnInfo(ExpenseCol.SERVICE_REGISTRY_ID) val serviceRegId: Long,
    @ColumnInfo(ExpenseCol.DATE) val date: LocalDate,
    @ColumnInfo(ExpenseCol.AMOUNT) val amount: Double
)

@Entity(foreignKeys = [ForeignKey(Service::class, ["id"], [ServiceRegistryCol.SERVICE_ID])])
data class ServiceRegistry(
    @ColumnInfo(ServiceRegistryCol.SERVICE_ID) val serviceId: Long,
    @ColumnInfo(ServiceRegistryCol.AMOUNT) val serviceAmount: Double,
    @ColumnInfo(ServiceRegistryCol.DEFAULT_AMOUNT) val defaultAmount: Double,
    @ColumnInfo(ServiceRegistryCol.START_DATE) val startDate: LocalDate,
    @ColumnInfo(ServiceRegistryCol.END_DATE) val endDate: LocalDate? = null,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

object ExpenseCol {
    const val SERVICE_REGISTRY_ID = "service_registry_id"
    const val DATE = "date"
    const val AMOUNT = "amount"
}

object ServiceCol {
    const val NAME = "name"
    const val TYPE = "type"
}

object ServiceRegistryCol {
    const val START_DATE = "start_date"
    const val END_DATE = "end_date"
    const val SERVICE_ID = "service_id"
    const val AMOUNT = "amount"
    const val DEFAULT_AMOUNT = "default_amount"
}

object DateRecurrenceCol {
    const val SERVICE_REGISTRY_ID = "service_registry_id"
    const val WEEK_DAY = "week_day"
}