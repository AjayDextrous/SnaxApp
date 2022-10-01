package com.makeathon.snax

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class CSVReader(context: Context, fileName: String) {
    var context: Context
    var fileName: String
    var rows: MutableList<List<String>> = ArrayList()

    init {
        this.context = context
        this.fileName = fileName
    }

    @Throws(IOException::class)
    fun readCSV(): MutableList<List<String>> {
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
            }
        }
        ins.close()
        return rows
    }
}