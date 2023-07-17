package com.spacey.myhome.data.expense

import com.spacey.myhome.data.ConnectivityHelper
import com.spacey.myhome.data.base.Data
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar

class ExpenseRepository(
    private val metadataRepository: MetadataRepository,
    private val connectivityHelper: ConnectivityHelper
) {
    fun getDateFields(date: Calendar): Flow<Data<List<FieldEntity>>> {
        return flow {
            delay(200)
            // Getting all meta for every date. after changing fields for date specific implement here.
            emit(metadataRepository.getMetadata(true).transform { it.fields })
        }

    }
}