package com.spacey.myhome.data.expense

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.spacey.myhome.data.metadata.FieldConstants
import com.spacey.myhome.data.metadata.FieldDBEntity

data class ExpenseEntity(
    @SerializedName(ExpenseConstants.Network.NAME) val name: String,
    @SerializedName(ExpenseConstants.Network.FIELD_ID) val fieldId: String,
    @SerializedName(ExpenseConstants.Network.type) val type: String,
    @SerializedName(ExpenseConstants.Network.VALUE) val value: String
)

// Expense retrofit network entity
data class DateExpensesEntity(
    @SerializedName(ExpenseConstants.Network.DATE) val date: String,
    @SerializedName(ExpenseConstants.Network.EXPENSES) val expenses: List<ExpenseEntity>
) {
    var isLatest = false
}

data class ExpenseRemoteEntity(
    @SerializedName(ExpenseConstants.Network.EXPENSES) val expenses: List<DateExpensesEntity>
) {
    fun getExpense(date: String): DateExpensesEntity =
        expenses.firstOrNull { it.date.replace("/0", "").replace("/", "") == date.replace("/", "") }
            ?: DateExpensesEntity(date, emptyList())
}

@Entity(
    tableName = ExpenseConstants.DB.TABLE, foreignKeys = [
        ForeignKey(
            entity = FieldDBEntity::class,
            parentColumns = [FieldConstants.DB.ID],
            childColumns = [ExpenseConstants.DB.FIELD_ID]
        )]
)
data class ExpenseDBEntity(
    @ColumnInfo(ExpenseConstants.DB.DATE) val date: String,
    val name: String,
    @ColumnInfo(ExpenseConstants.DB.FIELD_ID)
    val fieldId: String,
    val type: String,
    val value: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)