package fr.imacaron.crypto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

val textStyle = TextStyle(fontSize = TextUnit(25f, TextUnitType.Em))

data class Save(
    val a: Int,
    val b: Int,
    val c: Int,
    val d: Int,
    val mod: Int
)

@Composable
fun Matrix() {
    var matrix by remember { mutableStateOf(Matrix(null, null, null, null)) }
    var mod by remember { mutableStateOf("") }
    var det: Int? by remember { mutableStateOf(null) }
    var save: Save? by remember { mutableStateOf(null) }
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
                InputMatrixCard(matrix = matrix, onMatrixChange = { matrix = it })
                Text("%", style = textStyle)
                MatrixTextField(
                    value = mod,
                    onValueChange = { mod = it },
                    keyboardImeAction = ImeAction.Done,
                    keyboardAction = KeyboardActions(onDone = {
                        focus1.freeFocus()
                        focus2.freeFocus()
                        focus3.freeFocus()
                        focus4.freeFocus()
                        focus5.freeFocus()
                        matrix.apply {
                            save = Save(a!!, b!!, c!!, d!!, mod.toInt())
                            det = a * d - b * c
                        }
                    }),
                    focusRequester = focus5
                )
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                ElevatedButton(onClick = {
                    matrix = Matrix(null, null, null, null)
                    mod = ""
                    det = null
                    save = null
                    focus1.requestFocus()
                }) {
                    Text("Mr propre")
                }
                Button(
                    onClick = {
                        matrix.apply {
                            save = Save(a!!, b!!, c!!, d!!, mod.toInt())
                            det = a * d - b * c
                        }
                    },
                    enabled = matrix.run { a != null && b != null && c != null && d != null } && mod.isNotBlank(),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Let's go")
                }
            }
        }
        Card(Modifier.padding(8.dp)) {
            det?.let {
                Text(
                    "Le déterminant de la matrice est $it soit ${(it % save!!.mod + save!!.mod) % save!!.mod} % ${save!!.mod}",
                    Modifier.padding(8.dp)
                )
            }
        }
        Card(Modifier.padding(8.dp)) {
            det?.let {
                val data = euclide((it % save!!.mod + save!!.mod) % save!!.mod, save!!.mod)
                println(data)
                if (data.last().b == 1) {
                    MatrixCard(matrix = invMatrix(matrix))
                } else {
                    Text(
                        "Comme le déterminant de la matrice n'est pas inversable la matrice n'est pas inversable % ${save!!.mod}",
                        Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
