package com.devapp.fr.util

import com.devapp.fr.data.models.items.RadioItem

internal object DataHelper {
     internal fun getListChild() = listOf(
        RadioItem("Có vào một ngày nào đó"),
        RadioItem("Tôi sắp có"),
        RadioItem("Tôi không muốn có em bé"),
        RadioItem("Tôi đã có"),
        RadioItem("Tôi không muốn nói"),
    )

    internal fun getListDrink() = listOf(
        RadioItem("Có"),
        RadioItem("Không"),
        RadioItem("Thỉnh thoảng"),
        RadioItem("Tôi không muốn nói"),

        )
}