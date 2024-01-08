package com.climus.climeet.presentation.ui.intro.signup.climer

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetClimerLevelBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.climer.Adapter.LevelAdapter

class SetClimerLevelFragment :
    BaseFragment<FragmentSetClimerLevelBinding>(R.layout.fragment_set_climer_level) {

    private val viewModel: SetClimerLevelViewModel by viewModels()
    private lateinit var adapter: LevelAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        setupRecyclerView()

        viewModel.levels.observe(viewLifecycleOwner) { levels ->
            adapter.levels = levels
            adapter.notifyDataSetChanged()
        }

        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetClimerLevelEvent.NavigateToBack -> findNavController().navigateUp()
                    is SetClimerLevelEvent.NavigateToNext -> findNavController().toFollowCrag()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = LevelAdapter(viewModel)
        binding.rvLevel.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SetClimerLevelFragment.adapter
            addItemDecoration(AdapterDecoration())
        }
    }

    private fun NavController.toFollowCrag() {
        val action =
            SetClimerLevelFragmentDirections.actionSetClimerLevelFragmentToFollowCragFragment()
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