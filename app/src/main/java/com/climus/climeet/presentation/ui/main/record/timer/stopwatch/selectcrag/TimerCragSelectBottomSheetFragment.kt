package com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentTimerSelectCragBottomSheetBinding
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TimerCragSelectBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: TimerCragSelectBottomSheetViewModel by activityViewModels()
    private lateinit var binding: FragmentTimerSelectCragBottomSheetBinding
    private lateinit var sharedPreferences: SharedPreferences

    private var cragSearchAdapter: TimerCragSelectRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerSelectCragBottomSheetBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        sharedPreferences =
            requireContext().getSharedPreferences(TimerFragment.PREF_NAME, Context.MODE_PRIVATE)

        // 바텀시트 상단 모서리 radius 적용
        binding.layoutBottom.background = context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.rect_grey9fill_nostroke_top20radius
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerview()
        initStateObserve()
        initEventObserve()
    }

    private fun initRecyclerview() {
        cragSearchAdapter = TimerCragSelectRVAdapter(viewModel)
        binding.rvSearchCrag.layoutManager = LinearLayoutManager(context)
        binding.rvSearchCrag.adapter = cragSearchAdapter
    }

    private fun initStateObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                cragSearchAdapter?.setList(it.searchList, viewModel.keyword.value)
            }
        }
    }

    private fun initEventObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collect {
                when (it) {
                    is CragSelectEvent.NavigateToBack -> findNavController().navigateUp()
                    is CragSelectEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    fun showToastMessage(message: String) {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}