package fr.imacaron.crypto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Hill() {
    var matrix by remember { mutableStateOf(Matrix(null, null, null, null)) }
    var message by remember { mutableStateOf("") }
    var data: List<Int> by remember { mutableStateOf(listOf()) }
    var dataString by remember { mutableStateOf("[]") }
    var messageCoder by remember { mutableStateOf("") }
    var paquet by remember { mutableIntStateOf(1) }
    Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Card(Modifier.fillMaxWidth()) {
            Column(
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                InputMatrixCard(matrix = matrix, onMatrixChange = { matrix = it })
                Button(
                    onClick = {
                        if(message.isNotEmpty()){
                            data = hill(matrix, message, paquet)
                            dataString = data.joinToString(", ", "[", "]")
                            if(paquet == 1) {
                                messageCoder = data.joinToString(separator = "") { (xedoc[it]?.uppercaseChar() ?: '∅').toString() }
                            }
                        } else {
                            message = dehill(matrix, data, paquet)
                        }
                    },
                    enabled = paquet != 0 && matrix.isFull() && (message.isNotEmpty() || data.isNotEmpty())
                ) {
                    Text("Let's go")
                }
            }
        }
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(8.dp)) {
                OutlinedTextField(
                    value = if(paquet == 0) "" else paquet.toString(),
                    onValueChange = {
                        if(it == "") {
                            paquet = 0
                        } else {
                            it.toIntOrNull()?.let { int ->
                                if(int in 1..3) {
                                    paquet = int
                                }
                            }
                        }
                    },
                    label = { Text("Paquet (1, 2 ou 3)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                OutlinedTextField(
                    value = message,
                    onValueChange = {
                        message = it.toUpperCase(Locale.current)
                    },
                    label = { Text("Message") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
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
                                messageCoder = tmp.map { xedoc[it.toInt()]?.uppercaseChar() }.joinToString("")
                            }
                        }
                    },
                    label = { Text("Data") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { message = dehill(matrix, messageCoder.map { codex[it.lowercaseChar()] ?: 0}, paquet) })
                )
                if(paquet == 1) {
                    OutlinedTextField(
                        value = messageCoder,
                        onValueChange = { s ->
                            messageCoder = s
                            messageCoder.map { codex[it.lowercaseChar()] ?: 0 }.let { list ->
                                data = list
                                dataString = list.joinToString(", ", "[", "]")
                            }
                        },
                        label = { Text("Message coder") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
                    )
                }
            }
        }
    }
}