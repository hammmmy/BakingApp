package org.ipforsmartobjects.apps.baking.stepdetail;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import org.ipforsmartobjects.apps.baking.Injection;
import org.ipforsmartobjects.apps.baking.R;
import org.ipforsmartobjects.apps.baking.data.Step;
import org.ipforsmartobjects.apps.baking.databinding.RecipeStepDetailBinding;
import org.ipforsmartobjects.apps.baking.steppages.RecipeStepScreenSlideActivity;
import org.ipforsmartobjects.apps.baking.steps.RecipeStepsListActivity;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeStepsListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepScreenSlideActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment implements RecipeStepDetailContract.View,
        ExoPlayer.EventListener {

    public static final String ARG_STEP_ID = "step_id";

    public static final String ARG_RECIPE_ID = "recipe_id";

    public static final String ARG_TOTAL_STEPS = "total_steps";

    public static final String ARG_RECIPE_NAME = "recipe_name";

    private int mRecipeId;
    private int mStepId;
    private RecipeStepDetailBinding mBinding;
    private View mProgressBar;
    private View mDetailView;
    private View mEmptyView;
    private RecipeStepDetailPresenter mActionsListener;
    private CollapsingToolbarLayout mAppBarLayout;

    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;

    private PlaybackStateCompat.Builder mPlaybackStateBuilder;
    private SimpleExoPlayerView mExoPlayerView;
    private boolean mIsTwoPane;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP_ID)) {
            mRecipeId = (int) getArguments().getLong(ARG_RECIPE_ID);
            mStepId = (int) getArguments().getLong(ARG_STEP_ID);
        }

        mActionsListener = new RecipeStepDetailPresenter(this,
                Injection.provideRecipesRepository());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = RecipeStepDetailBinding.inflate(inflater, container, false);

        mProgressBar = mBinding.stepLayoutContainer.progress;
        mDetailView = mBinding.stepLayoutContainer.recipeDetailContainer;
        mEmptyView = mBinding.stepLayoutContainer.emptyView;
//        mAppBarLayout = mBinding.toolbarLayout;

        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);

        mExoPlayerView = mBinding.stepLayoutContainer.stepVideo;
        mIsTwoPane = getResources().getBoolean(R.bool.is_two_pane);


        mActionsListener.openStep(mRecipeId, mStepId);

        return mBinding.getRoot();
    }

    @Override
    public void setProgressIndicator(boolean active) {
        mProgressBar.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showEmptyView() {
        mDetailView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showStep(Step step) {
        mDetailView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        int orientation = getResources().getConfiguration().orientation;

        mBinding.toolbar.setTitle(step.getShortDescription());
//        if (mAppBarLayout != null) {
//            mAppBarLayout.setTitle(step.getShortDescription());
//        }
//        mBinding.detailToolbar.setTitle(step.getShortDescription());

        mBinding.stepLayoutContainer.stepDescription.setText(step.getDescription());

        if (!TextUtils.isEmpty(step.getThumbnailURL())) {
            Picasso.with(getActivity()).load(step.getThumbnailURL())
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(mBinding.stepLayoutContainer.stepVideoThumbnail);

        }

        String video = step.getVideoURL();

        if (!TextUtils.isEmpty(video)) {
            mBinding.stepLayoutContainer.stepVideo.setVisibility(View.VISIBLE);
            mBinding.stepLayoutContainer.stepDescription.setVisibility(View.VISIBLE);

            // Init and show video view
            initMediaSession();
            initExoPlayer(Uri.parse(video));

            // Expand to full screen in phone landscape
            if (orientation == Configuration.ORIENTATION_LANDSCAPE && !mIsTwoPane) {
                mBinding.stepLayoutContainer.stepDescription.setVisibility(View.GONE);
                expandVideoView();
                enterImmersiveMode();
            }
        } else {
            mBinding.stepLayoutContainer.stepVideo.setVisibility(View.GONE);
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.unbind();
    }

    private void initMediaSession() {
        mMediaSession = new MediaSessionCompat(getActivity(), "RecipeStepDetailFragment");

        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);
        mPlaybackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PAUSE
                        | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                        | PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mPlaybackStateBuilder.build());
        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                mExoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                mExoPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                mExoPlayer.seekTo(0);
            }
        });
        mMediaSession.setActive(true);
    }

    private void initExoPlayer(Uri mediaUri) {
        TrackSelector trackSelector = new DefaultTrackSelector();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
        mExoPlayerView.setPlayer(mExoPlayer);
        mExoPlayer.addListener(this);

        String userAgent = Util.getUserAgent(getActivity(), "StepVideo");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(false);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mPlaybackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mPlaybackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mPlaybackStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    private void enterImmersiveMode() {
        // using immersive UI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        Activity activity = getActivity();

        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar =((AppCompatActivity) activity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

    private void expandVideoView() {
        mExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        mExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }
}
