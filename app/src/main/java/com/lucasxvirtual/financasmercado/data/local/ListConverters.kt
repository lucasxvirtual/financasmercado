package com.lucasxvirtual.financasmercado.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ListConverters {
    @TypeConverter
    fun fromListIntToString(intList: List<Int>): String {
        val gson = Gson()
        return gson.toJson(intList)
    }

    @TypeConverter
    fun toListIntFromString(stringList: String): List<Int> {
        val listType: Type = object : TypeToken<ArrayList<Int>?>() {}.type
        return Gson().fromJson(stringList, listType)
    }
}
