package com.aic.aicdelivery;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeVideoAdapter.YoutubeViewHolder> {
    private static final String TAG = YoutubeVideoAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList;


    public YoutubeVideoAdapter(Context context, ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList) {
        this.context = context;
        this.youtubeVideoModelArrayList = youtubeVideoModelArrayList;
    }

    @Override
    public YoutubeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new YoutubeViewHolder(LayoutInflater.from(context).inflate(R.layout.youtube_box, parent, false));
    }

    @Override
    public void onBindViewHolder(YoutubeViewHolder holder, final int position) {
        final YoutubeVideoModel youtubeVideoModel = youtubeVideoModelArrayList.get(position);

        holder.videoTitle.setText(youtubeVideoModel.getTitle());
        holder.videoDuration.setText(youtubeVideoModel.getDuration());

        //open info window
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start youtube player activity by passing selected video id via intent
                context.startActivity(new Intent(context, youtubeplayer.class)
                        .putExtra("video_id", youtubeVideoModel.getVideoId()));

             /*   FragmentActivity activity = (FragmentActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("video_id", youtubeVideoModel.getVideoId());
                Fragment myFragment = new fr_youtubeplayer();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();*/
            }
        });

        /*  initialize the thumbnail image view , we need to pass Developer Key */
        holder.videoThumbnailImageView.initialize(HMConstants.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                //when initialization is sucess, set the video id to thumbnail to load
                youTubeThumbnailLoader.setVideo(youtubeVideoModel.getVideoId());

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        //print or show error when thumbnail load failed
                        Log.e(TAG, "Youtube Thumbnail Error");
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //print or show error when initialization failed
                Log.e(TAG, "Youtube Initialization Failure");

            }
        });

    }

    @Override
    public int getItemCount() {
        return youtubeVideoModelArrayList != null ? youtubeVideoModelArrayList.size() : 0;
    }

    public class YoutubeViewHolder extends RecyclerView.ViewHolder {

        public YouTubeThumbnailView videoThumbnailImageView;
        public TextView videoTitle, videoDuration;
        public CardView card_view;

        public YoutubeViewHolder(View itemView) {
            super(itemView);
            videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
            videoTitle = itemView.findViewById(R.id.video_title_label);
            videoDuration = itemView.findViewById(R.id.video_duration_label);
            card_view=itemView.findViewById(R.id.card_view);
        }
    }
}
