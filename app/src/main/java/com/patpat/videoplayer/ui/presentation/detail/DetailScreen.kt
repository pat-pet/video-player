package com.patpat.videoplayer.ui.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.patpat.videoplayer.domain.models.CommentModel
import com.patpat.videoplayer.domain.models.ContentVideoModel
import com.patpat.videoplayer.domain.usecases.utils.ResultState
import com.patpat.videoplayer.ui.components.VideoPlayer
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@UnstableApi
fun DetailScreen(
    contentVideo: ContentVideoModel?,
    commentList: ResultState<List<CommentModel>>,
    commentText: String,
    likes: String,
    dislikes: String,
    onCommentTextChanged: (String) -> Unit,
    onCommentSubmitted: () -> Unit,
    onBackPressed: () -> Unit,
    addLikes : () -> Unit,
    minLikes : () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val exoPlayer = remember(context) { ExoPlayer.Builder(context).build() }

    LaunchedEffect(key1 = true) {
        val video = MediaItem.fromUri(contentVideo?.url.orEmpty())
        val defaultDataSourceFactory = DefaultDataSource.Factory(context)
        val dataSourceFactory = DefaultDataSource.Factory(context, defaultDataSourceFactory)
        val source = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(video)
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
        exoPlayer.setMediaSource(source)
        exoPlayer.playWhenReady = true
        exoPlayer.prepare()
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                if (state == Player.STATE_BUFFERING) {
                    exoPlayer.pause()
                } else {
                    exoPlayer.play()
                }
            }
        })
    }

    DisposableEffect(exoPlayer) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> exoPlayer.play()
                Lifecycle.Event.ON_STOP -> exoPlayer.pause()
                else -> {
                    // Do nothing
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            exoPlayer.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Video Detail", style = MaterialTheme.typography.titleMedium)
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back Button Icon"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                Column(modifier = Modifier.padding(24.dp)) {
                    Box(modifier = Modifier.height(200.dp)) {
                        VideoPlayer(exoPlayer = exoPlayer)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF292929))
                            .clip(
                                RoundedCornerShape(
                                    topStart = CornerSize(0.dp),
                                    topEnd = CornerSize(0.dp),
                                    bottomStart = MaterialTheme.shapes.medium.bottomStart,
                                    bottomEnd = MaterialTheme.shapes.medium.bottomEnd
                                )
                            )
                            .padding(14.dp)
                    ) {
                        Text(
                            text = contentVideo?.title.orEmpty(),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = contentVideo?.subtitle.orEmpty())

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.End),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = addLikes) {
                                Icon(
                                    imageVector = Icons.Default.ThumbUp,
                                    contentDescription = "Like Icon"
                                )
                            }
                            Text(likes)
                            Spacer(
                                modifier = Modifier
                                    .height(height = 20.dp)
                                    .width(width = 2.dp)
                                    .background(color = MaterialTheme.colorScheme.onBackground)
                            )
                            IconButton(onClick = minLikes ) {
                                Icon(
                                    imageVector = Icons.Default.ThumbUp,
                                    contentDescription = "Like Icon",
                                    modifier = Modifier.rotate(180f)
                                )
                            }
                            Text(dislikes)
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .background(Color(0xFF292929))
                        .padding(14.dp)
                ) {
                    if (commentList is ResultState.Success) {
                        Text(
                            "Comments ${commentList.data.size}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    } else {
                        Text("Comments", style = MaterialTheme.typography.titleMedium)
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .background(Color(0xFF292929))
                        .padding(horizontal = 14.dp)
                        .padding(bottom = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(value = commentText, onValueChange = onCommentTextChanged)
                    IconButton(onClick = onCommentSubmitted) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send Button Icon"
                        )
                    }
                }
            }

            if (commentList is ResultState.Success) {
                items(commentList.data) { comment ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .background(Color(0xFF292929))
                            .padding(horizontal = 14.dp)
                            .padding(bottom = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Anonymous")
                            Text(
                                comment.createdAt.format(DateTimeFormatter.ISO_DATE),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        Text(text = comment.content)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview
@Composable
@UnstableApi
fun DetailScreenPreview() {
    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        DetailScreen(
            contentVideo = ContentVideoModel(
                "sq",
                "url",
                "title",
                "test",
                0,
                0,
                ""
            ),
            commentList = ResultState.Loading,
            onBackPressed = { },
            commentText = "",
            onCommentTextChanged = { },
            onCommentSubmitted = { },
            likes = "0",
            dislikes = "0",
            addLikes = {},
            minLikes = {}
        )
    }
}