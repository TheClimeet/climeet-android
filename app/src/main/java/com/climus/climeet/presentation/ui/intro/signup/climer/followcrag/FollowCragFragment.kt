package com.climus.climeet.presentation.ui.intro.signup.climer.followcrag

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentFollowCragBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.admin.searchname.SearchCragNameEvent
import com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.adapter.FollowCragRVAdapter
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class FollowCragFragment :
    BaseFragment<FragmentFollowCragBinding>(R.layout.fragment_follow_crag) {

    private val viewModel: FollowCragViewModel by viewModels()
    private var adapter: FollowCragRVAdapter? = null

    //private var cragList = ArrayList<Crag>()

    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        setRecycler()
        initStateObserve()
        initEventObserve()
        initSearchCrags()
    }

    private fun setRecycler(){
        adapter = FollowCragRVAdapter()
        binding.rvFollowCrags.adapter = adapter
        binding.rvFollowCrags.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.uiState.collect {
                adapter?.setList(it.searchList, viewModel.keyword.value)
                Log.d("FollowList", it.searchList.toString())
            }
        }
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is FollowCragEvent.NavigateToHowToKnow -> findNavController().toHowToKnow()
                    is FollowCragEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun NavController.toHowToKnow() {
        val action = FollowCragFragmentDirections.actionFollowCragFragmentToHowToKnowFragment3()
        navigate(action)
    }

    private fun initSearchCrags() {
        with(binding) {
            etSerachCrags.addTextChangedListener(object : TextWatcher {
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
                        rvFollowCrags.visibility = View.INVISIBLE
                        layoutSearchMiss.visibility = View.INVISIBLE
                        pbLoading.visibility = View.INVISIBLE
                    } else {
                        // 비어있지 않은 경우
                        searchJob = viewLifecycleOwner.lifecycleScope.launch {
                            rvFollowCrags.visibility = View.INVISIBLE
                            layoutSearchMiss.visibility = View.INVISIBLE
                            pbLoading.visibility = View.VISIBLE
                            delay(500)
                            // 검색 결과가 있는 경우
                            if (searchText == "더클라임") {
                                rvFollowCrags.visibility = View.VISIBLE
                                layoutSearchMiss.visibility = View.INVISIBLE
                                pbLoading.visibility = View.INVISIBLE
                            } else {
                                // 검색 결과가 없는 경우
                                rvFollowCrags.visibility = View.INVISIBLE
                                layoutSearchMiss.visibility = View.VISIBLE
                                pbLoading.visibility = View.INVISIBLE
                            }

                        }

                    }

                }

            })
        }
    }
}