package com.example.vps.ui.home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.vps.R;
import com.example.vps.ui.adapter.EmptyRecyclerView;
import com.example.vps.ui.adapter.NewsAdapter;
import com.example.vps.ui.adapter.PaginationListener;
import com.example.vps.ui.data.remote.response.NewsResponse;
import com.example.vps.ui.data.remote.response.Results;
import com.example.vps.ui.main.MainActivityViewModel;
import com.example.vps.ui.main.MainViewModelFactory;
import com.example.vps.ui.util.AnalyticsUtil;
import com.example.vps.ui.util.common.Category;
import com.example.vps.ui.util.common.Constants;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class HomeFragment extends Fragment  {


    private static final String NEWS_SECTION = "news_section";
    private MainActivityViewModel mainActivityViewModel;
    /** TextView that is displayed when the recycler view is empty */
    private TextView mEmptyStateTextView;
    /** Loading indicator that is displayed before the first load is completed */
    private View mLoadingIndicator;
    /** The {@link SwipeRefreshLayout} that detects swipe gestures and
     * triggers callbacks in the app.
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Results> newsResult = new ArrayList<>();

    private int currentPage = 1;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

    private static final String TAG = "HomeFragment";
    private NewsAdapter mAdapter;
    private EmptyRecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment getInstance(String newsSection) {
        HomeFragment fragment = new HomeFragment();
        if (newsSection == null) {
            return fragment;
        }
        Bundle bundle = new Bundle();
        bundle.putString(NEWS_SECTION, newsSection);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        registerObserver();
        if(getArguments() != null && getArguments().getString(NEWS_SECTION) != null){
            AnalyticsUtil.getInstance(getContext()).setCurrentScreen(getActivity(),getArguments().getString(NEWS_SECTION), "");
        } else {
            AnalyticsUtil.getInstance(getContext()).setCurrentScreen(getActivity(), Category.home.title, "");
        }
    }

    private void initViewModel() {
        if (getArguments() != null) {
            mainActivityViewModel = new ViewModelProvider(this,
                    new MainViewModelFactory(getActivity().getApplication(), getArguments().getString(NEWS_SECTION))).get(MainActivityViewModel.class);
        } else {
            mainActivityViewModel = new ViewModelProvider(this,
                    new MainViewModelFactory(getActivity().getApplication(), null)).get(MainActivityViewModel.class);
        }
        AnalyticsUtil.getInstance(getContext()).logEvent(FirebaseAnalytics.Event.SELECT_ITEM, getArguments());
    }

    private void registerObserver() {
        mainActivityViewModel.newsResult.observe(this, new Observer<NewsResponse>() {
            @Override
            public void onChanged(NewsResponse newsResponse) {
                if(newsResponse.getResults() !=null && newsResponse.getResults().size() > 0){
                    mLoadingIndicator.setVisibility(View.GONE);
                    mAdapter.addAll(newsResponse.getResults(), isLoading);
                    newsResult = newsResponse.getResults();
                    AnalyticsUtil.getInstance(getContext()).setUserProperty(FirebaseAnalytics.Event.VIEW_ITEM, String.valueOf(newsResult));
                } else {
                    // Otherwise, display error
                    // First, hide loading indicator so error message will be visible
                    mLoadingIndicator.setVisibility(View.GONE);
                    // Update empty state with no connection error message and image
                    mEmptyStateTextView.setText(R.string.no_data_found);
                    mEmptyStateTextView.setCompoundDrawablesWithIntrinsicBounds(Constants.DEFAULT_NUMBER,
                            R.drawable.ic_menu_slideshow,Constants.DEFAULT_NUMBER,Constants.DEFAULT_NUMBER);

                    // Hide SwipeRefreshLayout
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                }
                Timber.i("");
            }
        });
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // Find a reference to the {@link RecyclerView} in the layout
        // Replaced RecyclerView with EmptyRecyclerView
        mRecyclerView = root.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Find the SwipeRefreshLayout
        mSwipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        // Set the color scheme of the SwipeRefreshLayout
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipe_color_1),
                getResources().getColor(R.color.swipe_color_2),
                getResources().getColor(R.color.swipe_color_3),
                getResources().getColor(R.color.swipe_color_4));

        // Set up OnRefreshListener that is invoked when the user performs a swipe-to-refresh gesture.
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onRefresh() {
                Timber.i( "onRefresh called from SwipeRefreshLayout");
                // restart the loader
                initiateRefresh();
                Toast.makeText(getActivity(), getString(R.string.updated_just_now),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Find the loading indicator from the layout
        mLoadingIndicator = root.findViewById(R.id.loading_indicator);
        // Find the empty view from the layout and set it on the new recycler view
        mEmptyStateTextView = root.findViewById(R.id.empty_view);
        mRecyclerView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of news as input
        mAdapter = new NewsAdapter(getActivity(), new ArrayList<Results>());

        // Set the adapter on the {@link recyclerView}
        mRecyclerView.setAdapter(mAdapter);

        initPagination();

        // Check for network connectivity and initialize the loader
        initializeLoader(isConnected());

        return root;
    }

    private void initPagination() {
        mRecyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void loadMoreItems() {
              isLoading = true;
              currentPage++;
              totalPage = totalPage +10;
              mainActivityViewModel.initData(totalPage);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    /**
     * When the user returns to the previous screen by pressing the up button in the SettingsActivity,
     */
    @Override
    public void onResume() {
        super.onResume();
        if(mLoadingIndicator.getVisibility() == View.VISIBLE && newsResult.size() > 0){
            mAdapter.addAll(newsResult, isLoading);
            mLoadingIndicator.setVisibility(View.GONE);
        }
    }

    /**
     * If there is internet connectivity, initialize the loader as
     * usual. Otherwise, hide loading indicator and set empty state TextView to display
     * "No internet connection."
     *
     * @param connected internet connection is available or not
     */
    private void initializeLoader(boolean connected) {
        if (connected) {
            mLoadingIndicator.setVisibility(View.VISIBLE);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mLoadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message and image
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mEmptyStateTextView.setCompoundDrawablesWithIntrinsicBounds(Constants.DEFAULT_NUMBER,
                    R.drawable.ic_network_check, Constants.DEFAULT_NUMBER,Constants.DEFAULT_NUMBER);
        }
    }

    /**
     * Restart the loader if there is internet connectivity.
     * @param isConnected internet connection is available or not
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void restartLoader(boolean isConnected) {
        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            // Restart the loader with the NEWS_LOADER_ID
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mainActivityViewModel.initData(totalPage);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mLoadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message and image
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mEmptyStateTextView.setCompoundDrawablesWithIntrinsicBounds(Constants.DEFAULT_NUMBER,
                    R.drawable.ic_network_check,Constants.DEFAULT_NUMBER,Constants.DEFAULT_NUMBER);

            // Hide SwipeRefreshLayout
            mSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    /**
     * When the user performs a swipe-to-refresh gesture, restart the loader.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initiateRefresh() {
        restartLoader(isConnected());
    }


    /**
     *  Check for network connectivity.
     */
    private boolean isConnected() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onDestroyView() {
        mainActivityViewModel.clearAndSaveNewData();
        super.onDestroyView();
    }

}
