package fr.imacaron.crypto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Affine() {
    var a by remember { mutableStateOf("") }
    var b by remember { mutableStateOf("") }
    var paquet by remember { mutableIntStateOf(2) }
    var message by remember { mutableStateOf("") }
    var data by remember { mutableStateOf(listOf<Int>()) }
    var dataString by remember { mutableStateOf("[]") }
    var textCoder by remember { mutableStateOf("") }
    val focus1 = remember { FocusRequester() }
    val focus2 = remember { FocusRequester() }
    val focus3 = remember { FocusRequester() }
    Column(Modifier.padding(8.dp)) {
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                OutlinedTextField(
                    value = a,
                    onValueChange = { if (it == "-" || it.toIntOrNull() != null) a = it },
                    modifier = Modifier.focusRequester(focus1),
                    label = { Text("a") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focus2.requestFocus() })
                )
                OutlinedTextField(
                    value = b,
                    onValueChange = { if (it == "-" || it.toIntOrNull() != null) b = it },
                    modifier = Modifier.focusRequester(focus2),
                    label = { Text("b") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focus3.requestFocus() })
                )
                OutlinedTextField(
                    value = paquet.toString(),
                    onValueChange = {
                        if (it in listOf("1", "2", "3")) {
                            paquet = it.toInt()
                        }
                    },
                    label = { Text("Paquet") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(onClick = {
                        if (message.isNotBlank()) {
                            data = affinne(a.toInt(), b.toInt(), message, paquet)
                            dataString = data.joinToString(prefix = "[", postfix = "]", separator = ", ")
                            textCoder = data.map { xedoc[it]?.uppercaseChar() ?: '∅' }.joinToString("")
                        } else if (data.isNotEmpty()) {
                            message = deaffine(a.toInt(), b.toInt(), data, paquet)
                        }
                    }, enabled = a.isNotBlank() && b.isNotBlank() && (message.isNotBlank() || data.isNotEmpty())) {
                        Text("Let's go")
                    }
                }
            }
        }
        Card(
            Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it.toUpperCase(Locale.current) },
                    label = { Text("Message") },
                    modifier = Modifier.focusRequester(focus3),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters
                    )
                )
                OutlinedTextField(
                    value = dataString,
                    onValueChange = { s ->
                        var string = ""
                        if (!s.startsWith("[")) {
                            string = "["
                        }
                        string += s
                        if (!s.endsWith("]")) {
                            string += "]"
                        }
                        val tmp = string.removePrefix("[").removeSuffix("]").removeSuffix(",").split(",")
                        if (tmp.isEmpty() || !tmp.any { it.toIntOrNull() == null }) {
                            data = tmp.map { it.toInt() }
                            dataString = string
                            if (paquet == 1) {
                                textCoder = tmp.map { xedoc[it.toInt()]?.uppercaseChar() }.joinToString("")
                            }
                        }
                    },
                    modifier = Modifier.focusRequester(focus3),
                    label = { Text("Data") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { data = affinne(a.toInt(), b.toInt(), message, 1) })
                )
                if (paquet == 1) {
                    OutlinedTextField(
                        value = textCoder,
                        onValueChange = {
                            textCoder = it.toUpperCase(Locale.current)
                            data = it.toUpperCase(Locale.current).map { c -> codex[c.lowercaseChar()] ?: 0 }
                            dataString =
                                it.toUpperCase(Locale.current).map { c -> codex[c.lowercaseChar()] ?: 0 }.joinToString(", ", "[", "]")
                        },
                        label = { Text("Message Codé") },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { data = affinne(a.toInt(), b.toInt(), message, 1) })
                    )
                }
            }
        }
    }
}