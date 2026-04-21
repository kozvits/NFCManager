package com.nfcmanager.presentation

sealed class Screen(val route: String) {
    object Cards : Screen("cards")
    object Scan : Screen("scan")
    object CardDetail : Screen("card_detail/{cardId}") {
        fun createRoute(cardId: Long) = "card_detail/$cardId"
    }
}
