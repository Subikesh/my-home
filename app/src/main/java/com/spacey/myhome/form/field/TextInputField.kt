package com.spacey.myhome.form.field

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun TextInputField(label: String, textValue: String, modifier: Modifier = Modifier, keyboardType: KeyboardType = KeyboardType.Text, onTextChanged: (String) -> Unit) {
    Card(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text = label, modifier = Modifier.weight(1f))
            TextField(
                value = textValue,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType, capitalization = KeyboardCapitalization.Words),
                onValueChange = {
                    onTextChanged(it)
                },
                modifier = Modifier.width(150.dp)
            )
        }
    }
}