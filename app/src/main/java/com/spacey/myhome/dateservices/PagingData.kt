package com.spacey.myhome.dateservices

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.time.LocalDate

class DatePagingSource : PagingSource<LocalDate, LocalDate>() {
    override fun getRefreshKey(state: PagingState<LocalDate, LocalDate>): LocalDate? {
        return state.anchorPosition?.let { state.closestItemToPosition(it) }
    }

    override suspend fun load(params: LoadParams<LocalDate>): LoadResult<LocalDate, LocalDate> {
        val startDate = params.key
        val size = params.loadSize.toLong()
        return if (startDate == null) {
            Log.d("Date", "Is null")
            LoadResult.Invalid()
        } else {
            Log.d("Date", "Loaded from date: $startDate plus $size")
            LoadResult.Page(startDate.getNextNDays(size), startDate.minusDays(size), startDate.plusDays(size))
        }
    }

    private fun LocalDate.getNextNDays(n: Long): List<LocalDate> {
        return (0L until n).map { i ->
            plusDays(i)
        }
    }

}