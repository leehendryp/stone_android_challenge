package com.leehendryp.stoneandroidchallenge.feed.presentation.view

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_TEXT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.leehendryp.stoneandroidchallenge.R
import com.leehendryp.stoneandroidchallenge.core.BaseFragment
import com.leehendryp.stoneandroidchallenge.core.MainActivity
import com.leehendryp.stoneandroidchallenge.core.MyBadException
import com.leehendryp.stoneandroidchallenge.core.ServiceInstabilityException
import com.leehendryp.stoneandroidchallenge.core.StoneChallengeApplication
import com.leehendryp.stoneandroidchallenge.core.extensions.fadeIn
import com.leehendryp.stoneandroidchallenge.core.extensions.gone
import com.leehendryp.stoneandroidchallenge.core.extensions.observeOnMain
import com.leehendryp.stoneandroidchallenge.core.extensions.subscribeOnIO
import com.leehendryp.stoneandroidchallenge.core.extensions.vanish
import com.leehendryp.stoneandroidchallenge.core.extensions.visible
import com.leehendryp.stoneandroidchallenge.databinding.FragmentFeedBinding
import com.leehendryp.stoneandroidchallenge.feed.presentation.FreeTextSearcher
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Default
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Error
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Loading
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Success
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class FeedFragment : BaseFragment(), FreeTextSearcher {
    companion object {
        private const val SHARE_TYPE = "text/plain"
    }

    private lateinit var binding: FragmentFeedBinding

    private lateinit var feedAdapter: FeedAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: FeedViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_feed, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectDependencies()
        setUpFreeTextSearcher()
        initRecyclerView()
    }

    private fun setUpFreeTextSearcher() {
        (activity as MainActivity).freeTextSearcher = this
    }

    private fun injectDependencies() {
        (activity?.application as StoneChallengeApplication).appComponent.inject(this)
    }

    private fun initRecyclerView() {
        context?.let { context ->
            feedAdapter = FeedAdapter(
                context,
                mutableSetOf(),
                onClickShare = { joke ->
                    startShareIntent(
                        String.format(getString(R.string.share_intent_message), joke.url)
                    )
                }
            )
        }

        binding.listJokes.apply {
            adapter = feedAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun startShareIntent(value: String) {
        val sendIntent: Intent = Intent().apply {
            action = ACTION_SEND
            putExtra(EXTRA_TEXT, value)
            type = SHARE_TYPE
        }

        val shareIntent = Intent.createChooser(
            sendIntent, context?.getString(R.string.share_intent_title)
        )
        startActivity(shareIntent)
    }

    override fun onResume() {
        super.onResume()
        observeViewModelState()
    }

    override fun search(query: String) = viewModel.search(query)

    private fun observeViewModelState() {
        compositeDisposable.add(
            viewModel.state
                .subscribeOnIO()
                .observeOnMain()
                .subscribeBy { state -> updateUI(state) }
        )
    }

    private fun updateUI(state: FeedState) {
        toggleLoading(state)
        when (state) {
            Loading -> clearFeed()
            is Success -> updateFeed(state)
            is Error -> showErrorDialog(state)
            is Default -> showNoResultFeedback(state.shouldWarnNoResult)
        }
    }

    private fun toggleLoading(state: FeedState) {
        val animDuration: Long = 700

        binding.containerLoadingWheel.apply {
            if (state == Loading) fadeIn(animDuration) else vanish(animDuration)
        }
    }

    private fun clearFeed() = feedAdapter.clearList()

    private fun updateFeed(state: Success) {
        feedAdapter.update(state.data.toSet())
    }

    private fun showNoResultDialog() {
        createAlertDialog(
            title = R.string.feed_no_results,
            message = R.string.feed_try_new_keywords
        )?.show()
    }

    private fun showErrorDialog(state: Error) {
        when (state.error) {
            is MyBadException -> showErrorDialog(message = R.string.error_my_bad)
            is ServiceInstabilityException -> showErrorDialog(message = R.string.error_service_instability)
            is TimeoutException -> showErrorDialog(message = R.string.error_no_connection)
            is IOException -> showErrorDialog(message = R.string.error_no_connection)
            else -> showErrorDialog()
        }
    }

    private fun showNoResultFeedback(show: Boolean) {
        binding.textStandardMessage.apply {
            if (show) {
                visible()
                showNoResultDialog()
            } else {
                gone()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }
}
