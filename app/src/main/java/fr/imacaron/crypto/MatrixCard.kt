package fr.imacaron.crypto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

data class Matrix(
    val a: Int?,
    val b: Int?,
    val c: Int?,
    val d: Int?
) {
    fun isEmpty() = a == null && b == null && c == null && d == null

    fun isFull() = a != null && b != null && c != null && d != null
}

val parenthesisStyle = TextStyle(fontSize = TextUnit(20f, TextUnitType.Em))

@Composable
fun MatrixCard(matrix: Matrix, onMatrixChange: (Matrix) -> Unit) {
    var a by remember { mutableStateOf("") }
    var b by remember { mutableStateOf("") }
    var c by remember { mutableStateOf("") }
    var d by remember { mutableStateOf("") }
    LaunchedEffect(matrix) {
        a = matrix.a?.toString() ?: ""
        b = matrix.b?.toString() ?: ""
        c = matrix.c?.toString() ?: ""
        d = matrix.d?.toString() ?: ""
    }
    ElevatedCard {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("(", Modifier.padding(bottom = 16.dp), style = parenthesisStyle)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                MatrixTextField(value = a, onValueChange = {
                    if(it.isBlank() || it == "-" || it.toIntOrNull() != null) {
                        a = it
                        if(it.isBlank() || it == "-") {
                            onMatrixChange(Matrix(null, matrix.b, matrix.c, matrix.d))
                        }
                        it.toIntOrNull()?.let { int -> onMatrixChange(Matrix(int, matrix.b, matrix.c, matrix.d)) }
                    }
                })
                MatrixTextField(value = c, onValueChange = {
                    if(it.isBlank() || it == "-" || it.toIntOrNull() != null) {
                        c = it
                        if(it.isBlank() || it == "-") {
                            onMatrixChange(Matrix(matrix.a, matrix.b, null, matrix.d))
                        }
                        it.toIntOrNull()?.let { int -> onMatrixChange(Matrix(matrix.a, matrix.b, int, matrix.d)) }
                    }
                })
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                MatrixTextField(value = b, onValueChange = {
                    if(it.isBlank() || it == "-" || it.toIntOrNull() != null) {
                        b = it
                        if(it.isBlank() || it == "-") {
                            onMatrixChange(Matrix(matrix.a, null, matrix.c, matrix.d))
                        }
                        it.toIntOrNull()?.let { int -> onMatrixChange(Matrix(matrix.a, int, matrix.c, matrix.d)) }
                    }
                })
                MatrixTextField(value = d, onValueChange = {
                    if(it.isBlank() || it == "-" || it.toIntOrNull() != null) {
                        d = it
                        if(it.isBlank() || it == "-") {
                            onMatrixChange(Matrix(matrix.a, matrix.b, matrix.c, null))
                        }
                        it.toIntOrNull()?.let { int -> onMatrixChange(Matrix(matrix.a, matrix.b, matrix.c, int)) }
                    }
                })
            }
            Text(")", Modifier.padding(bottom = 16.dp), style = parenthesisStyle)
        }
    }
}

@Composable
fun MatrixTextField(
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
            .width(width)
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(4.dp))
            .padding(4.dp)
            .focusRequester(focusRequester),
        textStyle = TextStyle(
            fontSize = TextUnit(6f, TextUnitType.Em),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = keyboardImeAction),
        keyboardActions = keyboardAction,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSecondaryContainer)
    )
}
