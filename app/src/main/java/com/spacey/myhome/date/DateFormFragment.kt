package com.spacey.myhome.date

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.spacey.myhome.view.Field
import com.spacey.myhome.view.FormScreen

class DateFormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val formList = listOf(
                    Field.Picklist("Type", listOf("Daily", "Monthly"), "Daily"),
                    Field.Date("Date", System.currentTimeMillis()),
                    Field.Counter("Amount", 5),
                    Field.Text("Sample text")
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    FormScreen(formList) {
                        Log.d("Form", "Form submitted: $it")
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun PreviewForm() {
        Surface(modifier = Modifier.background(Color.White)) {
            Column {
                FormScreen(
                    listOf(
                        Field.Picklist("Type", listOf("Daily", "Monthly"), "Monthly"),
                        Field.Date("Date", 1697044878843),
                        Field.Counter("Amount"),
                        Field.Text("Sample text")
                    )
                ) {

                }
            }
        }
    }
}