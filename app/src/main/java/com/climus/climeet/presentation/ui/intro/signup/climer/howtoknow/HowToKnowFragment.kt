package com.climus.climeet.presentation.ui.intro.signup.climer.howtoknow

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentHowToKnowBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroViewModel

class HowToKnowFragment
    : BaseFragment<FragmentHowToKnowBinding>(R.layout.fragment_how_to_know) {

    private val parentViewModel: IntroViewModel by activityViewModels()
    private val viewModel: HowToKnowViewModel by viewModels()
    private lateinit var adapter: WayAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.climerSignUpProgress(5)
        binding.vm = viewModel
        setupRecyclerView()

        viewModel.levels.observe(viewLifecycleOwner) { ways ->
            adapter.ways = ways
            adapter.notifyDataSetChanged()
        }

        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is HowTokKnowEvent.NavigateToBack -> findNavController().navigateUp()
                    is HowTokKnowEvent.NavigateToNext -> findNavController().toNoticeSetting()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = WayAdapter(viewModel)
        binding.rvWay.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@HowToKnowFragment.adapter
            addItemDecoration(AdapterDecoration())
        }
    }

    private fun NavController.toNoticeSetting() {
        val action =
            HowToKnowFragmentDirections.actionHowToKnowFragmentToNoticeSettingFragment()
        navigate(action)
    }
}

class AdapterDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = 40
    }
}