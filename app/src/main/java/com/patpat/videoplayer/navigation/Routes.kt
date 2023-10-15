package com.patpat.videoplayer.navigation

sealed class Routes(val routeName: String) {
    object HomeScreen: Routes("home_screen")
    object DetailScreen: Routes("detail_screen") {
        const val CONTENT_VIDEO_ARGS = "CONTENT_VIDEO_ARGS"
    }
}