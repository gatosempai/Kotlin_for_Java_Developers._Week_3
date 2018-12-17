package nicestring

fun String.isNice(): Boolean {
    val substringOf = !this.contains("bu") && !this.contains("ba") && !contains("be")
    val containVowels = this.filter { c: Char -> (c in listOf('a', 'e', 'i', 'o', 'u')) }.count() >= 3
    val doubleLetter = this.containsADoubleLetter()
    return listOf(substringOf, containVowels, doubleLetter)
            .filter { it }.count() >= 2
}

fun String.containsADoubleLetter(): Boolean {
    val list1 = this.drop(1)
    val list2 = this.zip(list1)
    return list2.filter { (a, b) -> a == b }.count() > 0
}
