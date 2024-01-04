package com.tsypk.coreclient.service.presenting.table

import java.util.function.Consumer
import java.util.regex.Pattern

class TableList(columns: Int, vararg descriptions: String) {
    private val descriptions: Array<String>
    private val table: ArrayList<Array<String?>>
    private val tableSizes: IntArray
    private val rows: Int
    private var findex = 0
    private var filter: String?
    private var ucode: Boolean
    private var spacing: Int
    private val aligns: Array<EnumAlignment?>

    init {
        require(descriptions.size == columns)
        filter = null
        rows = columns
        this.descriptions = descriptions.toList().toTypedArray()
        table = ArrayList()
        tableSizes = IntArray(columns)
        updateSizes(descriptions.toList().toTypedArray())
        ucode = false
        spacing = 1
        aligns = arrayOfNulls(columns)
        for (i in aligns.indices) {
            aligns[i] = EnumAlignment.LEFT
        }
    }

    private fun updateSizes(elements: Array<String>) {
        for (i in tableSizes.indices) {
            var j = tableSizes[i]
            j = Math.max(j, elements[i].length)
            tableSizes[i] = j
        }
    }

    fun align(column: Int, align: EnumAlignment): TableList {
        aligns[column] = align
        return this
    }

    fun withSpacing(spacing: Int): TableList {
        this.spacing = spacing
        return this
    }

    fun addRow(vararg elements: String): TableList {
        require(elements.size == rows)
        table.add(elements.toList().toTypedArray())
        updateSizes(elements.toList().toTypedArray())
        return this
    }

    fun filterBy(par0: Int, pattern: String): TableList {
        findex = par0
        filter = pattern
        return this
    }

    fun withUnicode(ucodeEnabled: Boolean): TableList {
        ucode = ucodeEnabled
        return this
    }

    fun print(): String {
        val answerList: List<String>
        var answer = StringBuilder()
        var line: StringBuilder? = null
        if (ucode) {
            for (i in 0 until rows) {
                if (line != null) {
                    line.append(CROSSING_T)
                } else {
                    line = StringBuilder()
                    line.append(CORNER_TL)
                }
                for (j in 0 until tableSizes[i] + 2 * spacing) {
                    line.append(TLINE)
                }
            }
            line!!.append(CORNER_TR)
            answer.append(line.toString() + "\n")
            line = null
        }

        // header
        for (i in 0 until rows) {
            if (line != null) {
                line.append(gc(VERTICAL_TSEP))
            } else {
                line = StringBuilder()
                if (ucode) {
                    line.append(gc(VERTICAL_TSEP))
                }
            }
            var part = descriptions[i]
            while (part.length < tableSizes[i] + spacing) {
                part += " "
            }
            for (j in 0 until spacing) {
                line.append(" ")
            }
            line.append(part)
        }
        if (ucode) {
            line!!.append(gc(VERTICAL_TSEP))
        }
        answer.append(line.toString() + "\n")

        // print vertical separator
        line = null
        for (i in 0 until rows) {
            if (line != null) {
                line.append(gc(CROSSING))
            } else {
                line = StringBuilder()
                if (ucode) {
                    line.append(CROSSING_L)
                }
            }
            for (j in 0 until tableSizes[i] + 2 * spacing) {
                line.append(gc(BLINE))
            }
        }
        if (ucode) {
            line!!.append(CROSSING_R)
        }
        answer.append(line.toString() + "\n")
        line = null
        val localTable = table
        if (filter != null) {
            val p: Pattern = Pattern.compile(filter)
            localTable.removeIf { arr: Array<String?> ->
                val s = arr[findex]
                !p.matcher(s).matches()
            }
        }
        if (localTable.isEmpty()) {
            val sa = arrayOfNulls<String?>(rows)
            localTable.add(sa)
        }
        localTable.forEach(Consumer { arr: Array<String?> ->
            for (i in arr.indices) {
                if (arr[i] == null) {
                    arr[i] = ""
                }
            }
        })
        localTable.sortBy { it.first() ?: "" }
        for (strings in localTable) {
            for (i in 0 until rows) {
                if (line != null) {
                    line.append(gc(VERTICAL_BSEP))
                } else {
                    line = StringBuilder()
                    if (ucode) {
                        line.append(gc(VERTICAL_BSEP))
                    }
                }
                var part: String? = ""
                for (j in 0 until spacing) {
                    part += " "
                }
                if (strings[i] != null) {
                    when (aligns[i]) {
                        EnumAlignment.LEFT -> part += strings[i]
                        EnumAlignment.RIGHT -> {
                            var j = 0
                            while (j < tableSizes[i] - strings[i]!!.length) {
                                part += " "
                                j++
                            }
                            part += strings[i]
                        }

                        EnumAlignment.CENTER -> {
                            var j = 0
                            while (j < (tableSizes[i] - strings[i]!!.length) / 2) {
                                part += " "
                                j++
                            }
                            part += strings[i]
                        }

                        else -> {}
                    }
                }
                while (part!!.length < tableSizes[i] + spacing) {
                    part += " "
                }
                for (j in 0 until spacing) {
                    part += " "
                }
                line.append(part)
            }
            if (ucode) {
                line!!.append(gc(VERTICAL_BSEP))
            }
            answer.append(line.toString() + "\n")
            line = null
        }
        if (ucode) {
            for (i in 0 until rows) {
                if (line != null) {
                    line.append(CROSSING_B)
                } else {
                    line = StringBuilder()
                    line.append(CORNER_BL)
                }
                for (j in 0 until tableSizes[i] + 2 * spacing) {
                    line.append(gc(BLINE))
                }
            }
            line!!.append(CORNER_BR)
            answer.append(line.toString() + "\n")
        }
        return answer.toString()
    }

    private fun gc(src: Array<String>): String {
        return src[if (ucode) 1 else 0]
    }

    enum class EnumAlignment {
        LEFT, CENTER, RIGHT
    }

    companion object {
        private val BLINE = arrayOf("-", "\u2501")
        private val CROSSING = arrayOf("-+-", "\u2548")
        private val VERTICAL_TSEP = arrayOf("|", "\u2502")
        private val VERTICAL_BSEP = arrayOf("|", "\u2503")
        private const val TLINE = "\u2500"
        private const val CORNER_TL = "\u250c"
        private const val CORNER_TR = "\u2510"
        private const val CORNER_BL = "\u2517"
        private const val CORNER_BR = "\u251b"
        private const val CROSSING_L = "\u2522"
        private const val CROSSING_R = "\u252a"
        private const val CROSSING_T = "\u252c"
        private const val CROSSING_B = "\u253b"
    }
}
