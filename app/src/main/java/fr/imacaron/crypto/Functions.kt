package fr.imacaron.crypto

val codex = mapOf(
    'a' to 0,
    'b' to 1,
    'c' to 2,
    'd' to 3,
    'e' to 4,
    'f' to 5,
    'g' to 6,
    'h' to 7,
    'i' to 8,
    'j' to 9,
    'k' to 10,
    'l' to 11,
    'm' to 12,
    'n' to 13,
    'o' to 14,
    'p' to 15,
    'q' to 16,
    'r' to 17,
    's' to 18,
    't' to 19,
    'u' to 20,
    'v' to 21,
    'w' to 22,
    'x' to 23,
    'y' to 24,
    'z' to 25
)

val xedoc = mapOf(
    0 to 'a',
    1 to 'b',
    2 to 'c',
    3 to 'd',
    4 to 'e',
    5 to 'f',
    6 to 'g',
    7 to 'h',
    8 to 'i',
    9 to 'j',
    10 to 'k',
    11 to 'l',
    12 to 'm',
    13 to 'n',
    14 to 'o',
    15 to 'p',
    16 to 'q',
    17 to 'r',
    18 to 's',
    19 to 't',
    20 to 'u',
    21 to 'v',
    22 to 'w',
    23 to 'x',
    24 to 'y',
    25 to 'z'
)


fun getMod(paquet: Int) = when(paquet) {
    1 -> 26
    2 -> 2526
    3 -> 252526
    else -> throw IllegalArgumentException("paquet must be one two or three")
}

fun affinne(a: Int, b: Int, message: String, paquet: Int): List<Int> {
    var i = 0
    val res = mutableListOf<Int>()
    while(message.getOrNull(i) != null) {
        var value = 0
        for(j in 0 until paquet) {
            value *= 100
            value += codex[message.getOrNull(i + j)?.lowercaseChar()] ?: 0
        }
        res.add((a * value + b) % getMod(paquet))
        i += paquet
    }
    return res
}

fun deaffine(a: Int, b: Int, data: List<Int>, paquet: Int): String {
    val invA = euclide(a, getMod(paquet))[0].u
    val res = mutableListOf<Int>()
    var i = 0
    data.forEach {
        var tmp = (((it - b) * invA) % getMod(paquet) + 26) % getMod(paquet)
        for(j in 0 until paquet) {
            res.add(i, tmp % 100)
            tmp /= 100
        }
        i += paquet
    }
    return res.map { xedoc[it]?.uppercaseChar() }.joinToString("")
}

fun euclide(a: Int, b: Int): List<Euler> {
    val data = mutableListOf(Euler(a, b))
    recEuclide(data, 0)
    return data
}

fun recEuclide(data: MutableList<Euler>, i: Int) {
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
        recEuclide(data, i + 1)
        data[i].u = data[i + 1].v
        data[i].v = -data[i].q * data[i].u + data[i + 1].u
    }
}

data class Euler(
    val a: Int,
    val b: Int,
    var r: Int = 0,
    var q: Int = 0,
    var u: Int = 0,
    var v: Int = 0
)

fun hill(key: Matrix, message: String, paquet: Int): List<Int> {
    var i = 0
    val res = mutableListOf<Int>()
    var tmp: Int? = null
    while(message.getOrNull(i) != null || tmp != null) {
        var value = 0
        for(j in 0 until paquet) {
            value *= 100
            value += codex[message.getOrNull(i + j)?.lowercaseChar()] ?: 0
        }
        tmp = if((i / paquet) % 2 == 1) {
            res.add((key.a!! * tmp!! + key.b!! * value) % getMod(paquet))
            res.add((key.c!! * tmp + key.d!! * value) % getMod(paquet))
            null
        } else {
            value
        }
        i += paquet
    }
    return res
}

fun dehill(key: Matrix, data: List<Int>, paquet: Int): String {
    val detKey = det(key)
    val dataDetKey = euclide(detKey, getMod(paquet))
    if(dataDetKey.last().b != 1) {
        return ""
    }
    val invDetKey = dataDetKey[0].u
    val invKey = invMatrix(key).run {
        Matrix((a!! * invDetKey) % getMod(paquet), (b!! * invDetKey) % getMod(paquet), (c!! * invDetKey) % getMod(paquet), (d!! * invDetKey) % getMod(paquet))
    }
    println("det = $detKey, invDet = $invDetKey")
    var tmp: Int? = null
    val res = mutableListOf<Int>()
    var i = 0
    while(data.getOrNull(i) != null) {
        tmp = if(tmp == null) {
            println(i)
            println(data.getOrNull(i))
            data.getOrNull(i) ?: 0
        } else {
            println("tmp = $tmp & data = ${data.getOrNull(i) ?: 0} & matrix = $invKey")
            var tmp1 = invKey.a!! * tmp + invKey.b!! * (data.getOrNull(i) ?: 0)
            for(j in 0 until paquet) {
                res.add(i - 1, (tmp1 modMath getMod(paquet) ) modMath  100)
                tmp1 /= 100

            }
            tmp1 = invKey.c!! * tmp + invKey.d!! * (data.getOrNull(i) ?: 0)
            for(j in 0 until paquet) {
                res.add(i, (tmp1 modMath getMod(paquet)) modMath 100)
                tmp1 /= 100
            }
            null
        }
        i++
    }
    println(res)
    return res.map { xedoc[it]?.uppercaseChar() }.joinToString("")
}

fun det(a: Matrix): Int {
    return a.a!! * a.d!! - a.b!! * a.c!!
}

fun invMatrix(matrix: Matrix): Matrix = Matrix(matrix.d!!, -matrix.b!!, -matrix.c!!, matrix.a!!)

infix fun Int.modMath(other: Int): Int = ((this % other) + other) % other