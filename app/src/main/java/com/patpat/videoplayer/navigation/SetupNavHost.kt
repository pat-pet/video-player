package com.patpat.videoplayer.navigation

import android.net.Uri
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.patpat.videoplayer.domain.models.ContentVideoModel
import com.patpat.videoplayer.navigation.Routes.DetailScreen.CONTENT_VIDEO_ARGS
import com.patpat.videoplayer.ui.presentation.detail.DetailScreen
import com.patpat.videoplayer.ui.presentation.detail.DetailViewModel
import com.patpat.videoplayer.ui.presentation.home.HomeScreen
import com.patpat.videoplayer.ui.presentation.home.HomeViewModel

@Composable
@UnstableApi
fun SetupNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen.routeName
    ) {
        composable(Routes.HomeScreen.routeName) {
            val viewModel = hiltViewModel<HomeViewModel>()
            val contentVideos = viewModel.contentVideos

            LaunchedEffect(key1 = true) {
                viewModel.initData()
            }

            HomeScreen(
                videos = contentVideos,
                onClick = {
                    val json = Uri.encode(Gson().toJson(it))
                    navController.navigate(Routes.DetailScreen.routeName + "/$json")
                },
                isLoading = viewModel.isLoading
            )
        }

        composable(
            Routes.DetailScreen.routeName + "/{$CONTENT_VIDEO_ARGS}",
            arguments = listOf(
                navArgument(CONTENT_VIDEO_ARGS) {
                    type = AssetParamType()
                }
            ),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            val video = it.arguments?.getParcelable<ContentVideoModel>(CONTENT_VIDEO_ARGS)
            val viewModel = hiltViewModel<DetailViewModel>()
            val comments = viewModel.comments
            val comment = viewModel.commentText

            LaunchedEffect(key1 = true) {
                viewModel.initData(video?.id.orEmpty())
            }

            LaunchedEffect(key1 = true) {
                viewModel.observeVideo(videoId = video?.id.orEmpty())
            }

            DetailScreen(
                contentVideo = video,
                commentText = comment,
                onCommentTextChanged = viewModel::updateComment,
                onCommentSubmitted = {
                    viewModel.sendComment(video?.id.orEmpty())
                },
                onBackPressed = {
                    navController.popBackStack()
                },
                commentList = comments,
                likes = viewModel.likes,
                dislikes = viewModel.dislikes,
                addLikes = {
                    viewModel.addLikes(video?.id.orEmpty())
                },
                minLikes = {
                    viewModel.minLikes(video?.id.orEmpty())
                },
            )
        }
    }
}
