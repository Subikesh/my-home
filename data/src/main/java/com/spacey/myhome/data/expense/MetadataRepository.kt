package com.spacey.myhome.data.expense

import com.spacey.myhome.data.ConnectivityHelper
import com.spacey.myhome.data.base.Data
import com.spacey.myhome.data.expense.local.FieldDBEntity
import com.spacey.myhome.data.expense.local.MetadataLocalDataSource
import com.spacey.myhome.data.expense.remote.MetadataRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MetadataRepository(
    private val metadataRemoteDataSource: MetadataRemoteDataSource,
    private val metadataLocalDataSource: MetadataLocalDataSource,
    private val connectivityHelper: ConnectivityHelper
) {
    suspend fun getMetadata(offline: Boolean = false): Data<MetadataEntity> = withContext(Dispatchers.IO) {
        try {
            if (!offline && connectivityHelper.isInternetConnected()) {
                val metadata = metadataRemoteDataSource.getMetadata()
                metadataLocalDataSource.insertFieldsMetadata(metadata.fields.map { it.toFieldDBEntity() })
            }
            Data.Success(MetadataEntity(metadataLocalDataSource.getFieldsMetadata()))
        } catch (e: Exception) {
            Data.Error(exception = e)
        }
    }

    private fun FieldEntity.toFieldDBEntity(): FieldDBEntity = FieldDBEntity(id, name, type, input)
}