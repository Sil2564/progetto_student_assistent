package com.silvianikikarim.studentassistant.model

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromTipoNota(tipo: TipoNota): String = tipo.name

    @TypeConverter
    fun toTipoNota(value: String): TipoNota = TipoNota.valueOf(value)
}
