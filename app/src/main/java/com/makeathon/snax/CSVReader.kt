package com.makeathon.snax

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

enum class Info{
    CALORIES, PROTEIN, CARBS, SUGAR, FAT, SATURATED, ALTERNATIVE
}

class CSVReader(context: Context, fileName: String) {
    var context: Context
    var fileName: String
    var rows: MutableList<List<String>> = ArrayList()

    init {
        this.context = context
        this.fileName = fileName
    }

    @Throws(IOException::class)
    fun readCSV(): MutableMap<String, MutableMap<Info,String>> {

        val info = mutableMapOf<String, MutableMap<Info, String>>()
        val ins: InputStream = context.assets.open(fileName)
        val isr = InputStreamReader(ins)
        val br = BufferedReader(isr)
        var line: String?
        val csvSplitBy = ","
        br.readLine()
        while (br.readLine().also { line = it } != null) {
            val row = line?.split(csvSplitBy)
            if (row != null) {
                rows.add(row)
                info[row[0]] = mutableMapOf()
                info[row[0]]?.set(Info.CALORIES, row[1])
                info[row[0]]?.set(Info.PROTEIN, row[2])
                info[row[0]]?.set(Info.CARBS, row[3])
                info[row[0]]?.set(Info.SUGAR, row[4])
                info[row[0]]?.set(Info.FAT, row[5])
                info[row[0]]?.set(Info.SATURATED, row[6])
                info[row[0]]?.set(Info.ALTERNATIVE, row[7])
            }
        }
        ins.close()
        return info
    }
}