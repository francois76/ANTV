package fr.fgognet.antv.utils

fun cleanDescription(rawDescription: String?): String? {
    if (rawDescription == null) {
        return null
    }
    // base cleaning of description
    var result = rawDescription.replace("<br>", "\n").replace("–", "-").trim()
    // replace line separator ;-
    if (result != "" && "-" == result.subSequence(0, 1)) {
        result = result.replace(";-", "\n-")
    }
    // replace end of line separator ;
    result = result.replace(";\n", "\n")
    // adding cariage return with "-"
    result = result.replace(":-", ":\n-")
    // formatting dot list with . :
    result = result.replace(":.", ":\n•").replace(",.", "\n•")
    // format line with •
    result = result.replace(";•", "•").replace("•", "\n•")
    // if ; is used as line separator, it is replaced
    if (result.split("\n-").size == 1) {
        result = result.replace(";", "\n- ")
    }
    return result
}