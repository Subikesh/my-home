package com.spacey.myhome.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spacey.myhome.ui.component.CardView
import com.spacey.myhome.ui.component.Field
import com.spacey.myhome.utils.HomeDatePickerRow
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(
    selectedDate: LocalDate,
    expenseList: State<List<Field<*>>>,
    fab: @Composable () -> Unit,
    onDateChanged: (LocalDate) -> Unit
) {
    var date: LocalDate by remember { mutableStateOf(selectedDate) }


    LaunchedEffect(date) {
        onDateChanged(date)
    }
    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = { fab() }) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalItemSpacing = 24.dp
        ) {
            item(span = StaggeredGridItemSpan.Companion.FullLine) {
                HomeDatePickerRow(initialDate = date,
                    onDateChanged = { newDate ->
                        date = newDate
                    }) {
                    Text(
                        "${date.dayOfMonth} ${
                            date.month.getDisplayName(
                                TextStyle.SHORT,
                                Locale.getDefault()
                            )
                        } ${date.year},\n${date.dayOfWeek}",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.displayMedium
                    )
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Date edit",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            items(expenseList.value) { field ->
                field.CardView()
            }
        }

    }

}

@Preview
@Composable
fun Preview() {
    Surface(Modifier.background(Color.White)) {
        Column(Modifier.padding(16.dp)) {
            Field.Counter("Sample", 10).CardView()
            Field.CheckBox("True? False?").CardView()
        }
    }
}