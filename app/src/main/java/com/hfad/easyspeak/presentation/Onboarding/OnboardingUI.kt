package com.hfad.easyspeak.presentation.Onboarding


import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.media3.common.util.UnstableApi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.boundsInRoot
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.hfad.easyspeak.R
import com.hfad.easyspeak.presentation.components.CustomButton
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes

@Composable
fun VideoPlayerScreen(
    modifier: Modifier = Modifier,
    navController: NavController // Добавляем параметр
) {
    Box(modifier = modifier.fillMaxSize()) {
        VideoPlayerCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            navController = navController // Передаем navController
        )
    }
}

@Composable
private fun VideoPlayerCard(
    modifier: Modifier = Modifier,
    navController: NavController // Добавляем параметр
) {
    val aspectRatio = 280f / 500f

    Box(
        modifier = modifier
            .aspectRatio(aspectRatio)
            .padding(bottom = 20.dp)
            .rotate(-3f)
            .shadow(
                elevation = 8.dp,
                shape = MaterialTheme.shapes.large
            )
            .background(
                color = Color.White,
                shape = MaterialTheme.shapes.large
            )
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.colorScheme.surface
                    )
                ),
                shape = MaterialTheme.shapes.large
            )
    ) {
        VideoPlayer(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.large),
            navController = navController // Передаем navController
        )
    }
}
@OptIn(UnstableApi::class)
@Composable
private fun VideoPlayer(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    if (LocalInspectionMode.current) return

    val context = LocalContext.current
    val view = LocalView.current
    var player by remember { mutableStateOf<ExoPlayer?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var hasPlayedOnce by remember { mutableStateOf(false) }

    // Обработчик жизненного цикла активности
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleObserver = remember {
        object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                player?.pause()
            }
            override fun onStop(owner: LifecycleOwner) {
                player?.pause()
            }
            override fun onDestroy(owner: LifecycleOwner) {
                player?.release()
                player = null
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            player?.release()
            player = null
        }
    }

    DisposableEffect(Unit) {
        val packageName = context.packageName
        val resourceId = context.resources.getIdentifier("video", "raw", packageName)

        if (resourceId == 0) {
            errorMessage = "Видеофайл не найден в res/raw/video"
        } else {
            try {
                player = ExoPlayer.Builder(context).build().apply {
                    val rawUri = Uri.parse("android.resource://$packageName/$resourceId")
                    setMediaItem(MediaItem.fromUri(rawUri))

                    addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            if (playbackState == Player.STATE_ENDED && !hasPlayedOnce) {
                                hasPlayedOnce = true
                                navController.navigate(NavigationRoutes.PleaseRegistration.route)
                            }
                        }
                    })

                    playWhenReady = true
                    repeatMode = Player.REPEAT_MODE_OFF
                    prepare()
                }
            } catch (e: Exception) {
                errorMessage = "Ошибка при воспроизведении видео: ${e.localizedMessage}"
            }
        }

        onDispose {
            player?.release()
        }
    }

    var isVideoFullyVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .onGloballyPositioned { coordinates ->
                val bounds = coordinates.boundsInRoot()
                isVideoFullyVisible = bounds.top > 0 &&
                        bounds.bottom < view.height &&
                        bounds.left > 0 &&
                        bounds.right < view.width
            }
    ) {
        when {
            errorMessage != null -> Text(
                text = errorMessage!!,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
            player != null -> {
                val currentPlayer = player!!
                LaunchedEffect(isVideoFullyVisible) {
                    if (isVideoFullyVisible) currentPlayer.play()
                    else currentPlayer.pause()
                }

                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            this.player = currentPlayer
                            useController = false
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                MuteButton(
                    player = currentPlayer,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                )
            }
            else -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center))
        }
    }
}
@Composable
fun MuteButton(
    player: Player,
    modifier: Modifier = Modifier
) {
    var isMuted by remember(player) {
        mutableStateOf(player.volume == 0f)
    }

    LaunchedEffect(player) {
        snapshotFlow { player.volume }.collect { volume ->
            isMuted = volume == 0f
        }
    }

    OutlinedIconButton(
        onClick = {
            isMuted = !isMuted
            try {
                player.volume = if (isMuted) 0f else 1f
            } catch (e: Exception) {
                println("Error setting volume: ${e.message}")
            }
        },
        modifier = modifier.size(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = IconButtonDefaults.outlinedIconButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.8f)
        )
    ) {
        Icon(
            painter = painterResource(
                if (isMuted) R.drawable.ic_volume_off else R.drawable.ic_volume_up
            ),
            contentDescription = if (isMuted) "Unmute" else "Mute",
            modifier = Modifier.size(24.dp)
        )
    }
}
    @Composable
    fun VideoPlayerPreview(navController: NavController) {
        Scaffold(
            bottomBar = {
                CustomButton(
                    modifier = Modifier.padding(bottom = 60.dp, start = 10.dp, end = 10.dp),
                    title = "Next",
                    onClick = {
                        navController.navigate(NavigationRoutes.PleaseRegistration.route)
                    }

                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                VideoPlayerScreen(navController = navController)
            }

        }


    }