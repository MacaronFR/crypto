package fr.imacaron.crypto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly

val textStyle = TextStyle(fontSize = TextUnit(25f, TextUnitType.Em))
val textStyle2 = TextStyle(fontSize = TextUnit(10f, TextUnitType.Em))

@Composable
fun Hill() {
    var a by remember { mutableStateOf("") }
    var b by remember { mutableStateOf("") }
    var c by remember { mutableStateOf("") }
    var d by remember { mutableStateOf("") }
    var mod by remember { mutableStateOf("") }
    var det: Int? by remember { mutableStateOf(null) }
    val focus1 = remember { FocusRequester() }
    val focus2 = remember { FocusRequester() }
    val focus3 = remember { FocusRequester() }
    val focus4 = remember { FocusRequester() }
    val focus5 = remember { FocusRequester() }
    Column {
        Card(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("(", style = textStyle)
                Column(Modifier.padding(horizontal = 4.dp, vertical = 8.dp)) {
                    Row(Modifier.padding(4.dp)) {
                        MinimalTextField(
                            value = a,
                            onValueChange = {
                                a = it
                            },
                            keyboardImeAction = ImeAction.Next,
                            keyboardAction = KeyboardActions(onNext = { focus2.requestFocus() }),
                            focusRequester = focus1
                        )
                        MinimalTextField(
                            value = b, onValueChange = {
                                b = it
                            },
                            keyboardImeAction = ImeAction.Next,
                            keyboardAction = KeyboardActions(onNext = { focus3.requestFocus() }),
                            focusRequester = focus2
                        )
                    }
                    Row(Modifier.padding(4.dp)) {
                        MinimalTextField(
                            value = c, onValueChange = {
                                c = it
                            },
                            keyboardImeAction = ImeAction.Next,
                            keyboardAction = KeyboardActions(onNext = { focus4.requestFocus() }),
                            focusRequester = focus3
                        )
                        MinimalTextField(
                            value = d, onValueChange = {
                                d = it
                            },
                            keyboardImeAction = ImeAction.Next,
                            keyboardAction = KeyboardActions(onNext = { focus5.requestFocus() }),
                            focusRequester = focus4
                        )
                    }
                }
                Text(")% ", style = textStyle)
                MinimalTextField(
                    value = mod,
                    onValueChange = { mod = it },
                    keyboardImeAction = ImeAction.Done,
                    keyboardAction = KeyboardActions(onDone = {
                        focus1.freeFocus()
                        focus2.freeFocus()
                        focus3.freeFocus()
                        focus4.freeFocus()
                        focus5.freeFocus()
                        det = a.toInt() * d.toInt() - b.toInt() * c.toInt()
                    }),
                    focusRequester = focus5
                )
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                ElevatedButton(onClick = {
                    a = ""
                    b = ""
                    c = ""
                    d = ""
                    mod = ""
                    det = null
                    focus1.requestFocus()
                } ) {
                    Text("Mr propre")
                }
                Button(onClick = { det = a.toInt() * d.toInt() - b.toInt() * c.toInt() }, enabled = a.isNotBlank() && b.isNotBlank() && c.isNotBlank() && d.isNotBlank() && mod.isNotBlank(), modifier = Modifier.padding(start = 8.dp)) {
                    Text("Let's go")
                }
            }
        }
        Card(Modifier.padding(8.dp)) {
            det?.let {
                Text(
                    "Le déterminant de la matrice est $it soit ${(it % mod.toInt() + mod.toInt()) % mod.toInt()} % $mod",
                    Modifier.padding(8.dp)
                )
            }
        }
        Card(Modifier.padding(8.dp)) {
            det?.let {
                Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("(", style = textStyle)
                    Column(Modifier.padding(end = 12.dp)) {
                        Text(d, style = textStyle2)
                        Text("${-b.toInt()}", style = textStyle2)
                    }
                    Column {
                        Text("${-c.toInt()}", style = textStyle2)
                        Text(a, style = textStyle2)
                    }
                    Text(")", style = textStyle)
                }
            }
        }
    }
}

@Composable
fun MinimalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    width: Dp = 60.dp,
    keyboardImeAction: ImeAction = ImeAction.None,
    keyboardAction: KeyboardActions = KeyboardActions(),
    focusRequester: FocusRequester = FocusRequester()
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .width(width)
            .background(MaterialTheme.colorScheme.background)
            .focusRequester(focusRequester),
        textStyle = TextStyle(
            fontSize = TextUnit(8f, TextUnitType.Em),
            color = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = keyboardImeAction),
        keyboardActions = keyboardAction
    )
}
