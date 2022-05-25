package com.zeroboss.scoring500.data.common

import com.zeroboss.scoring500.presentation.common.Trump
import com.zeroboss.scoring500.presentation.common.trumpFromString
import io.objectbox.converter.PropertyConverter

class TrumpConverter : PropertyConverter<Trump?, String?> {
    override fun convertToEntityProperty(text: String?): Trump {
        return trumpFromString(text!!)
    }

    override fun convertToDatabaseValue(trump: Trump?): String {
        return trump!!.toString()
    }
}
