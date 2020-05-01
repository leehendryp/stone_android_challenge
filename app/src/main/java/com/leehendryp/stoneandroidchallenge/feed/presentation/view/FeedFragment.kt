package com.leehendryp.stoneandroidchallenge.feed.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leehendryp.stoneandroidchallenge.R
import com.leehendryp.stoneandroidchallenge.core.BadRequestException
import com.leehendryp.stoneandroidchallenge.core.MainActivity
import com.leehendryp.stoneandroidchallenge.core.NotFoundException
import com.leehendryp.stoneandroidchallenge.core.RequestTimeoutException
import com.leehendryp.stoneandroidchallenge.core.UnauthorizedException
import com.leehendryp.stoneandroidchallenge.core.extensions.observeOnMain
import com.leehendryp.stoneandroidchallenge.core.extensions.subscribeOnIO
import com.leehendryp.stoneandroidchallenge.databinding.FragmentFeedBinding
import com.leehendryp.stoneandroidchallenge.feed.presentation.FreeTextSearcher
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Error
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Loading
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Success
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.net.UnknownHostException
import javax.inject.Inject

class FeedFragment : Fragment(), FreeTextSearcher {
    private lateinit var binding: FragmentFeedBinding

    private lateinit var feedAdapter: FeedAdapter
    private lateinit var recyclerView: RecyclerView

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
        (activity as MainActivity).freeTextSearcher = this
        initRecyclerView()
    }

    private fun initRecyclerView() {
        context?.let { context ->
            feedAdapter = FeedAdapter(
                context,
                mutableSetOf(),
                onClickShare = { Unit }
            )
        }

        recyclerView = binding.listJokes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = feedAdapter
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

    private fun updateFeed(state: Success) {
        (recyclerView.adapter as FeedAdapter).update(state.data.toSet())
    }

    private fun showErrorDialog(state: Error) {
        when (state.error) {
            is BadRequestException -> toastGenericErrorMessage()
            is UnauthorizedException -> toastGenericErrorMessage()
            is NotFoundException -> toastGenericErrorMessage()
            is RequestTimeoutException -> toastGenericErrorMessage()
            is UnknownHostException -> toastConnectionErrorMessage()
        }
    }

    private fun toastGenericErrorMessage() {
        Toast.makeText(
            context,
            getString(R.string.generic_error),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun toastConnectionErrorMessage() {
        Toast.makeText(
            context,
            getString(R.string.no_connection_error),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun clearFeed() = (recyclerView.adapter as FeedAdapter).clearList()

    private fun toggleLoading(state: FeedState) = if (state == Loading) Unit else Unit

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }
}
