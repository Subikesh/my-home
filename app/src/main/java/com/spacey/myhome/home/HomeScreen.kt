package com.spacey.myhome.home

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spacey.myhome.MyHomeViewModel
import com.spacey.myhome.ui.component.CardView
import com.spacey.myhome.ui.component.Field
import com.spacey.myhome.utils.HomeDatePickerRow
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(viewModel: MyHomeViewModel) {
    var selectedDate: LocalDate by remember { mutableStateOf(LocalDate.now()) }
    val cardList = buildList {
        val services = listOf(
            Field.Counter("Milk", 1),
            Field.Counter("Water can", 0),
            Field.CheckBox("Gas"),
//            Field.Amount("Maid")
        )
        for (i in 1..10) {
            addAll(services)
        }
    }
    viewModel.setDate(selectedDate)
    val expenseList = viewModel.expenseList.collectAsState(initial = emptyList())
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalItemSpacing = 24.dp
    ) {
        item(span = StaggeredGridItemSpan.Companion.FullLine) {
            HomeDatePickerRow(initialDate = selectedDate,
                onDateChanged = { selectedDate = it }) {
                Text(
                    "${selectedDate.dayOfMonth} ${
                        selectedDate.month.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        )
                    } ${selectedDate.year},\n${selectedDate.dayOfWeek}",
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

//@Preview
//@Composable
//fun Preview() {
//    Surface(Modifier.background(Color.White)) {
//        Column(Modifier.padding(16.dp)) {
//            Field.Counter("Sample", 10).CardView()
//            Field.CheckBox("True? False?").CardView()
//        }
//    }
//}