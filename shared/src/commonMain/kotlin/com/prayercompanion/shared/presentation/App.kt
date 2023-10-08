package com.prayercompanion.shared.presentation

import androidx.compose.runtime.Composable
import com.prayercompanion.shared.presentation.features.onboarding.splash_screen.SplashScreen
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme

@Composable
fun App() {
    PrayerCompanionAndroidTheme {
        SplashScreen()
    }
}

//@Composable
//fun App() {
//    val SqlDateTimeFormatter = LocalDateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.en())
//    val FullDateTimeFormatter: LocalDateTimeFormatter = LocalDateTimeFormatter
//        .ofPattern("dd/MM/yyyy - HH:mm", Locale.en())
//    val MonthYearFormatter: LocalDateTimeFormatter = LocalDateTimeFormatter
//        .ofPattern("MM/yyyy", Locale.en())
//    val DateFormatter: LocalDateTimeFormatter = LocalDateTimeFormatter
//        .ofPattern("dd/MM/yyyy", Locale.en())
//    val TimeFormatter: LocalDateTimeFormatter = LocalDateTimeFormatter
//        .ofPattern("HH:mm", Locale.en())
//
//    PrayerCompanionAndroidTheme {
//        Box {
//            AppBackground()
//            Column {
//                listOf(
//                    {
//                        val formatter = FullDateTimeFormatter
//                        val localDateTime = LocalDateTime(2021, 1, 1, 1, 1)
//                        equals("01/01/2021 - 01:01", formatter.format(localDateTime))
//                    },
//                    {
//                        val formatter = MonthYearFormatter
//                        val localDateTime = LocalDateTime(2021, 1, 1, 1, 1)
//                        equals("01/2021", formatter.format(localDateTime))
//                    },
//                    {
//                        val formatter = DateFormatter
//                        val localDateTime = LocalDate(2021, 1, 1)
//                        equals("01/01/2021", formatter.format(localDateTime))
//                    },
//                    {
//                        val formatter = TimeFormatter
//                        val localDateTime = LocalTime(1, 1, 1)
//                        equals("01:01", formatter.format(localDateTime))
//                    },
//                    {
//                        val formatter = SqlDateTimeFormatter
//                        val localDateTime = LocalDateTime(2021, 1, 1, 1, 1)
//                        equals(
//                            formatter.parseToLocalDateTime("2021-01-01 01:01:00").toString(),
//                            localDateTime.toString()
//                        )
//                    },
//                ).forEach {
//                    Text(text = it())
//                }
//            }
//        }
//    }
//}

fun equals(a: Any, b: Any): String {
    return (a == b).toString()
}