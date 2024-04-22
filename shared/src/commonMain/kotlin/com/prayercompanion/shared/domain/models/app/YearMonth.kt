package com.prayercompanion.shared.domain.models.app

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.number

data class YearMonth(val year: Int, val month: Month) {
    fun atDay(day: Int): LocalDate {
        return LocalDate(year, month, day)
    }

    fun atEndOfMonth(): LocalDate {
        val lastDay = month.number.monthLength(isLeapYear(year))
        return LocalDate(year, month, lastDay)
    }

    fun plusMonths(months: Int): YearMonth {
        val newMonth = month.number + months
        val newYear = year + (newMonth - 1) / 12
        val newMonthNumber = (newMonth - 1) % 12 + 1
        return YearMonth(newYear, newMonthNumber.toMonth())
    }

    fun minusMonths(months: Int): YearMonth {
        val totalMonths = year * 12 + (month.number - 1) - months
        val newYear = totalMonths / 12
        val newMonth = (totalMonths % 12) + 1 // Adding 1 because months are 1-based

        return YearMonth(newYear, newMonth.toMonth())
    }

    private fun Int.toMonth(): Month {
        return Month.values()[this - 1]
    }

    private fun isLeapYear(year: Int): Boolean {
        val prolepticYear: Long = year.toLong()
        return prolepticYear and 3 == 0L && (prolepticYear % 100 != 0L || prolepticYear % 400 == 0L)
    }

    private fun Int.monthLength(isLeapYear: Boolean): Int {
        return when (this) {
            2 -> if (isLeapYear) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
    }

    companion object {
        fun from(localDate: LocalDate): YearMonth {
            return YearMonth(localDate.year, localDate.month)
        }
    }
}