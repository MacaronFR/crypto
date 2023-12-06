package fr.imacaron.crypto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Euclide() {
    var key by remember { mutableStateOf("") }
    var mod by remember { mutableStateOf("") }
    var data by remember { mutableStateOf(listOf<Euler>()) }
    val focus = remember { FocusRequester() }
    val focus2 = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column {
        Card(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column(
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    modifier = Modifier.focusRequester(focus),
                    value = key,
                    onValueChange = {
                        if (it.isDigitsOnly()) {
                            key = it
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focus2.requestFocus()
                    })
                )
                Text("%")
                OutlinedTextField(
                    modifier = Modifier.focusRequester(focus2), value = mod, onValueChange = {
                        if (it.isDigitsOnly()) {
                            mod = it
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focus2.freeFocus()
                        data = euclide(key.toInt(),mod.toInt())
                        keyboardController?.hide()
                    })
                )
                Row(Modifier.padding(8.dp)) {
                    ElevatedButton(onClick = {
                        data = listOf()
                        key = ""
                        mod = ""
                        focus.requestFocus()
                    }) {
                        Text("Mr. Propre")
                    }
                    Button(modifier = Modifier.padding(start = 8.dp), onClick = {
                        data = euclide(key.toInt(), mod.toInt())
                        keyboardController?.hide()
                        focus.freeFocus()
                        focus2.freeFocus()
                    }, enabled = key.isNotBlank() && mod.isNotBlank()) {
                        Text("Let's go")
                    }
                }
            }
        }
        Card(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            LazyColumn {
                item {
                    Row {
                        TableCell(text = "a")
                        TableCell(text = "b")
                        TableCell(text = "r")
                        TableCell(text = "q")
                        TableCell(text = "u")
                        TableCell(text = "v")
                    }
                }
                items(data) {
                    val (a, b, r, q, u, v) = it
                    Row {
                        TableCell(text = a.toString())
                        TableCell(text = b.toString())
                        TableCell(text = r.toString())
                        TableCell(text = q.toString())
                        TableCell(text = u.toString())
                        TableCell(text = v.toString())
                    }
                }
            }
        }
        Card(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (data.isNotEmpty()) {
                Column(Modifier.padding(8.dp)) {
                    Text("${data[0].a} % ${data[0].b} = ${data[0].r}")
                    if(data.last().b == 1) {
                        Text("L'inverse modulaire de ${data[0].a} % ${data[0].b} est ${data[0].u}")
                    } else {
                        Text("Il n'y a pas d'inverse modulaire pour ${data[0].a} % ${data[0].b}")
                    }
                }
            }
        }
    }
}