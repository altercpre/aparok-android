package io.aparok.app.presentation.editor.editor.pattern

interface Matcher<out T> {
    fun match(text: String): List<T>
}