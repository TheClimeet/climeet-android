package com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentTimerBottomsheetCragBinding
import com.climus.climeet.presentation.ui.main.record.model.RecordCragData
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

interface CragSelectionListener {
    fun onCragSelected(crag: RecordCragData)
}

class CragSelectBottomFragment : BottomSheetDialogFragment() {

    private val viewModel: CragSelectBottomViewModel by viewModels()
    private lateinit var binding: FragmentTimerBottomsheetCragBinding
    private lateinit var sharedPreferences: SharedPreferences

    private var cragSearchAdapter: CragSelectRVAdapter? = null
    var cragSelectionListener: CragSelectionListener? = null

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerBottomsheetCragBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        sharedPreferences = requireContext().getSharedPreferences(TimerFragment.PREF_NAME, Context.MODE_PRIVATE)

        // 바텀시트 상단 모서리 radius 적용
        binding.layoutBottom.background = context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.rect_grey9fill_nostroke_20radius
            )
        }

        binding.btnDelete.setOnClickListener {
            binding.etCragName.text.clear()
            Log.d("timer", "삭제 후 : ${binding.etCragName.text}")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerview()
        initStateObserve()
        initSearchCrags()
    }

    private fun initRecyclerview() {
        cragSearchAdapter = CragSelectRVAdapter { cragName ->
            // 선택된 암장 정보 저장
            viewModel.setSelectedItem(cragName)
            cragSelectionListener?.onCragSelected(cragName) // TimerFragment로 데이터 전달
            sharedPreferences.edit().putString("cragName", cragName.name).apply()
            // 바텀시트 닫기
            dismiss()
        }
        binding.rvSearchCrag.layoutManager = LinearLayoutManager(context)
        binding.rvSearchCrag.adapter = cragSearchAdapter
    }

    private fun initStateObserve() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                cragSearchAdapter?.setList(it.searchList, viewModel.keyword.value)
            }
        }
    }

    private fun initSearchCrags() {
        with(binding) {
            etCragName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // 텍스트 변경 전
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    // 텍스트가 변경 중
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(500)
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    searchJob?.cancel() // 이전 검색 작업 취소

                    val searchText = s.toString().toLowerCase(Locale.getDefault())
                    Log.d("SearchTextText", "searchText : $searchText")

                    // 비어있음
                    if (searchText.isEmpty()) {
                        rvSearchCrag.visibility = View.INVISIBLE
                        layoutSearchNone.visibility = View.INVISIBLE
                        pbLoading.visibility = View.INVISIBLE
                    } else {
                        // 비어있지 않은 경우
                        searchJob = viewLifecycleOwner.lifecycleScope.launch {
                            rvSearchCrag.visibility = View.INVISIBLE
                            layoutSearchNone.visibility = View.INVISIBLE
                            pbLoading.visibility = View.VISIBLE
                            delay(500)
                            // 검색 결과가 있는 경우
                            if (searchText == "더클라임") {
                                rvSearchCrag.visibility = View.VISIBLE
                                layoutSearchNone.visibility = View.INVISIBLE
                                pbLoading.visibility = View.INVISIBLE
                            } else {
                                // 검색 결과가 없는 경우
                                rvSearchCrag.visibility = View.INVISIBLE
                                layoutSearchNone.visibility = View.VISIBLE
                                pbLoading.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
            })
        }
    }
}