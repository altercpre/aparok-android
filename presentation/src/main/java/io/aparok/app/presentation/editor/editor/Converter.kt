package io.aparok.app.presentation.editor.editor

interface Converter<in INPUT, out OUTPUT> {
    fun convert(input: INPUT): OUTPUT
}