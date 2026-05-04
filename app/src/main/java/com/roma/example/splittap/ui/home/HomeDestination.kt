package com.roma.example.splittap.ui.home

import androidx.annotation.StringRes
import com.roma.example.splittap.R

enum class HomeDestination(
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    val iconRes: Int
) {
    Dashboard(R.string.home_dashboard_title, R.string.home_dashboard_subtitle, R.drawable.ic_dashboard_24),
    Groups(R.string.home_groups_title, R.string.home_groups_subtitle, R.drawable.ic_groups_24),
    Roommates(R.string.home_roommates_title, R.string.home_roommates_subtitle, R.drawable.ic_person_add_24),
    Expenses(R.string.home_expenses_title, R.string.home_expenses_subtitle, R.drawable.ic_receipt_24),
    Activity(R.string.home_activity_title, R.string.home_activity_subtitle, R.drawable.ic_activity_24),
    Settings(R.string.home_settings_title, R.string.home_settings_subtitle, R.drawable.ic_settings_24)
}