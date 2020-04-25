package com.example.vps.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vps.R;
import com.example.vps.ui.data.remote.response.Results;
import com.example.vps.ui.detail.WebViewActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import timber.log.Timber;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Results> newsList;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    public NewsAdapter(Context context, ArrayList<Results> results) {
        this.context = context;
        this.newsList = results;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_NORMAL: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
                return new NewsViewHolder(view);
            }
            case VIEW_TYPE_LOADING: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
                return new LoadingViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
            return position == newsList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NewsViewHolder){
            populateItemRows((NewsViewHolder)holder, position);
        }
        else if(holder instanceof LoadingViewHolder){
            showLoadingView((LoadingViewHolder)holder, position);
        }
    }

    private void showLoadingView(LoadingViewHolder holder, int position) {
        holder.progressBar.setVisibility(View.VISIBLE);
    }

    private void populateItemRows(NewsViewHolder holder, int position) {
        // Find the current news that was clicked on
        final Results currentNews = newsList.get(position);

        holder.titleTextView.setText(currentNews.getWebTitle());
        holder.sectionTextView.setText(currentNews.getSectionName());
        // Get time difference between the current date and web publication date and
        // set the time difference on the textView
        holder.dateTextView.setText(getTimeDifference(formatDate(currentNews.getWebPublicationDate())));

        // Get string of the trailTextHTML and convert Html text to plain text
        // and set the plain text on the textView
        if(currentNews.getFields() != null){
            String trailTextHTML = currentNews.getFields().getTrailText();
            holder.trailTextView.setText(Html.fromHtml(Html.fromHtml(trailTextHTML).toString()));
        }


        // Set an OnClickListener to open a website with more information about the selected article
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getWebUrl());

                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(context, WebViewActivity.class);
                websiteIntent.putExtra(WebViewActivity.URL, currentNews.getWebUrl());

                // Send the intent to launch a new activity
                context.startActivity(websiteIntent);
            }
        });

        if (currentNews.getFields() == null) {
            holder.thumbnailImageView.setVisibility(View.GONE);
        } else {
            holder.thumbnailImageView.setVisibility(View.VISIBLE);
            // Load thumbnail with glide
            Glide.with(context.getApplicationContext())
                    .load(currentNews.getFields().getThumbnail())
                    .into(holder.thumbnailImageView);
        }
        // Set an OnClickListener to share the data with friends via email or  social networking
        holder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareData(currentNews);
            }
        });

    }

    private void shareData(Results currentNews) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                currentNews.getWebTitle() + " : " + currentNews.getWebUrl());
        context.startActivity(Intent.createChooser(sharingIntent,
                context.getString(R.string.share_article)));

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


    /**
     * Get the time difference between the current date and web publication date
     * @param formattedDate the formatted web publication date string
     * @return time difference (i.e "9 hours ago")
     */
    private CharSequence getTimeDifference(String formattedDate) {
        long currentTime = System.currentTimeMillis();
        long publicationTime = getDateInMillis(formattedDate);
        return DateUtils.getRelativeTimeSpanString(publicationTime, currentTime,
                DateUtils.SECOND_IN_MILLIS);
    }


    /**
     * Get the formatted web publication date string in milliseconds
     * @param formattedDate the formatted web publication date string
     * @return the formatted web publication date in milliseconds
     */
    @SuppressLint("TimberArgCount")
    private static long getDateInMillis(String formattedDate) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("MMM d, yyyy  h:mm a");
        long dateInMillis;
        Date dateObject;
        try {
            dateObject = simpleDateFormat.parse(formattedDate);
            dateInMillis = dateObject.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            Timber.e("Problem parsing date", e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * Convert date and time in UTC (webPublicationDate) into a more readable representation
     * in Local time
     *
     * @param dateStringUTC is the web publication date of the article (i.e. 2014-02-04T08:00:00Z)
     * @return the formatted date string in Local time(i.e "Jan 1, 2000  2:15 AM")
     * from a date and time in UTC
     */
    private String formatDate(String dateStringUTC) {
        // Parse the dateString into a Date object
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
        Date dateObject = null;
        try {
            dateObject = simpleDateFormat.parse(dateStringUTC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Initialize a SimpleDateFormat instance and configure it to provide a more readable
        // representation according to the given format, but still in UTC
        SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy  h:mm a", Locale.ENGLISH);
        String formattedDateUTC = df.format(dateObject);
        // Convert UTC into Local time
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(formattedDateUTC);
            df.setTimeZone(TimeZone.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df.format(date);
    }

    /**
     *  Clear all data (a list of  objects)
     */
    public void clearAll() {
        newsList.clear();
        notifyDataSetChanged();
    }

    /**
     * Add  a list of
     * @param mNewsList is the list of news, which is the data source of the adapter
     * @param loading
     */
    public void addAll(List<Results> mNewsList, boolean loading) {
        newsList.clear();
        newsList.addAll(mNewsList);
        isLoaderVisible = loading;
        notifyDataSetChanged();
    }


    class NewsViewHolder extends RecyclerView.ViewHolder  {
        private TextView titleTextView;
        private TextView sectionTextView;
        private TextView authorTextView;
        private TextView dateTextView;
        private ImageView thumbnailImageView;
        private ImageView shareImageView;
        private TextView trailTextView;
        private CardView cardView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_card);
            sectionTextView = itemView.findViewById(R.id.section_card);
            authorTextView = itemView.findViewById(R.id.author_card);
            dateTextView = itemView.findViewById(R.id.date_card);
            thumbnailImageView = itemView.findViewById(R.id.thumbnail_image_card);
            shareImageView = itemView.findViewById(R.id.share_image_card);
            trailTextView = itemView.findViewById(R.id.trail_text_card);
            cardView = itemView.findViewById(R.id.card_view);
        }

    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);

        }

    }
}
