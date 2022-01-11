package com.androidcamp.liveflix.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.androidcamp.liveflix.databinding.ActivityVideoBinding;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, Player.Listener {
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";
    protected PlayerView playerView;
    protected @Nullable SimpleExoPlayer player;
    private boolean startAutoPlay;
    private int startWindow;
    private long startPosition;
    private ActivityVideoBinding binding;
    private List<MediaItem> mediaItems;
    private String vUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize(savedInstanceState);
        vUrl = getIntent().getStringExtra("URL");
        binding.channelName.setText(getIntent().getStringExtra("NAME"));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        releasePlayer();
        clearStartPosition();
        setIntent(intent);
    }

    private void initialize(Bundle savedInstanceState) {
        playerView = binding.playerView;

        if (savedInstanceState != null) {
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        } else {
            clearStartPosition();
        }
    }

    private void initializePlayer() {
        if (player == null) {
            mediaItems = createMediaItems();
            player = new SimpleExoPlayer.Builder(this).setMediaSourceFactory(new DefaultMediaSourceFactory(this).setLiveTargetOffsetMs(30_000)).build();
            player.setPlayWhenReady(startAutoPlay);
            playerView.setPlayer(player);
        }

        boolean haveStartPosition = startWindow != C.INDEX_UNSET;

        if (haveStartPosition) {
            player.seekTo(startWindow, startPosition);
        }

        player.addListener(this);
        player.setMediaItems(mediaItems, !haveStartPosition);
//        player.seekTo(channelPosition(), C.INDEX_UNSET);
        player.prepare();
        player.setPlayWhenReady(true);
    }

    protected void releasePlayer() {
        if (player != null) {
            updateStartPosition();
            player.release();
            player = null;
            mediaItems = Collections.emptyList();
        }
    }

    private void updateStartPosition() {
        if (player != null) {
            startAutoPlay = player.getPlayWhenReady();
            startWindow = player.getCurrentWindowIndex();
            startPosition = Math.max(0, player.getContentPosition());
        }
    }

    protected void clearStartPosition() {
        startAutoPlay = true;
        startWindow = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }

    @Override
    public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
        assert mediaItem == null || mediaItem.mediaMetadata.title != null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        updateStartPosition();
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        outState.putInt(KEY_WINDOW, startWindow);
        outState.putLong(KEY_POSITION, startPosition);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }


    @Override
    public void onIsPlayingChanged(boolean isPlaying) {
        if (isPlaying) {
            binding.animationView.setVisibility(View.GONE);
        } else {
            binding.animationView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    private List<MediaItem> createMediaItems() {
        List<MediaItem> mediaItems = new ArrayList<>();
        mediaItems.add(setMediaItem());
        return mediaItems;
    }

    private MediaItem setMediaItem() {
        return new MediaItem.Builder()
                .setUri(Uri.parse(vUrl))
                .setTag("Red")
                .setMediaMetadata(new MediaMetadata.Builder()
                        .setTitle("")
                        .setMediaUri(Uri.parse(vUrl))
                        .setArtworkUri(Uri.parse(""))
                        .setArtist("").build())
                .build();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUi();
        }
    }

    private void hideSystemUi() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onClick(View v) {

    }
}