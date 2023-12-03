package fr.imacaron.crypto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import fr.imacaron.crypto.ui.theme.CryptoTheme

data class Euler(
    val a: Int,
    val b: Int,
    var r: Int = 0,
    var q: Int = 0,
    var u: Int = 0,
    var v: Int = 0
)

@Composable
fun RowScope.TableCell(
    text: String
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(1f / 6)
            .padding(8.dp)
    )
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var key by remember { mutableStateOf("") }
            var mod by remember { mutableStateOf("") }
            var data by remember { mutableStateOf(listOf<Euler>()) }
            val focus = remember { FocusRequester() }
            val focus2 = remember { FocusRequester() }
            val keyboardController = LocalSoftwareKeyboardController.current
            CryptoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
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
                                        data = euler(key.toInt(),mod.toInt())
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
                                        data = euler(key.toInt(), mod.toInt())
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
            }
        }
    }
}

fun euler(a: Int, b: Int): List<Euler> {
    val data = mutableListOf(Euler(a, b))
    recEuler(data, 0)
    return data
}

fun recEuler(data: MutableList<Euler>, i: Int) {
    val n = data[i].run {
        r = a % b
        q = a / b
        if (r != 0) {
            Euler(b, r)
        } else {
            if (b == 1) {
                u = 0
                v = 1
            }
            null
        }
    }
    n?.let {
        data.add(it)
        recEuler(data, i + 1)
        data[i].u = data[i + 1].v
        data[i].v = -data[i].q * data[i].u + data[i + 1].u
    }
}