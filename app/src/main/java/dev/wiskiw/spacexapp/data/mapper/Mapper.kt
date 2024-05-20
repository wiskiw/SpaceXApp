package dev.wiskiw.spacexapp.data.mapper

import dev.wiskiw.spacexapp.data.model.MapperException

abstract class Mapper<From, To> {

    companion object {
        @Throws(MapperException::class)
        fun <Field> Field?.expectValue(
            fieldName: String,
        ): Field {
            val message = "The value of the field '$fieldName' is expected, but it's null"
            return this ?: throw MapperException(message)
        }
    }

    @Throws(MapperException::class)
    abstract fun map(from: From): To

}
