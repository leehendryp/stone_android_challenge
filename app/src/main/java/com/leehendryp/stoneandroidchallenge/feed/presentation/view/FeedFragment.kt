package com.leehendryp.stoneandroidchallenge.feed.presentation.view

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
import com.leehendryp.stoneandroidchallenge.core.extensions.observeOnMain
import com.leehendryp.stoneandroidchallenge.core.extensions.subscribeOnIO
import com.leehendryp.stoneandroidchallenge.core.extensions.vanish
import com.leehendryp.stoneandroidchallenge.databinding.FragmentFeedBinding
import com.leehendryp.stoneandroidchallenge.feed.presentation.FreeTextSearcher
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState
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
                onClickShare = { Unit }
            )
        }

        binding.listJokes.apply {
            adapter = feedAdapter
            layoutManager = LinearLayoutManager(context)
        }
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
        with (state.data) {
            if (this.isEmpty()) showNoResultDialog()

            feedAdapter.update(this.toSet())
        }
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

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }
}
