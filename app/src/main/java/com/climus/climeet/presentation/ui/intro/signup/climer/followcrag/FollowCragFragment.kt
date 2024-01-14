package com.climus.climeet.presentation.ui.intro.signup.climer.followcrag

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentFollowCragBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.adapter.CustomSpinnerAdaptor
import com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.adapter.FollowCragRVAdapter
import com.climus.climeet.presentation.ui.intro.signup.climer.model.Crag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class FollowCragFragment : BaseFragment<FragmentFollowCragBinding>(R.layout.fragment_follow_crag) {

    private var cragList = ArrayList<Crag>()

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivFollowCragsBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnNextValid.setOnClickListener {
            findNavController().toClimbingGoal()
        }
        initSearchCrags()
    }

    private fun initSearchCrags() {
        with(binding) {
            dummy()
            val followCragRVAdapter = FollowCragRVAdapter(cragList)
            rvFollowCrags.adapter = followCragRVAdapter
            rvFollowCrags.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

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
//        val sortOptions = listOf("가나다순","팔로우순")

//        binding.spinner.adapter = CustomSpinnerAdaptor(requireActivity(), R.layout.item_spinner_sort_option, sortOptions)
//        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                val value = binding.spinner.getItemAtPosition(p2).toString()
//            }
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                // 선택되지 않은 경우
//            }
//        }

    private fun dummy() {
        cragList.apply {
            clear()
            add(Crag(null, "더클라임 클라이밍 강남점", 70, false))
            add(Crag(null, "더클라임 클라이밍 논현점", 30, false))
            add(Crag(null, "더클라임 클라이밍 마곡점", 120, false))
            add(Crag(null, "더클라임 클라이밍 홍대점", 12, false))
            add(Crag(null, "더클라임 클라이밍 강남점", 70, false))
            add(Crag(null, "더클라임 클라이밍 논현점", 30, false))
            add(Crag(null, "더클라임 클라이밍 마곡점", 120, false))
            add(Crag(null, "더클라임 클라이밍 홍대점", 12, false))
            add(Crag(null, "더클라임 클라이밍 강남점", 70, false))
            add(Crag(null, "더클라임 클라이밍 논현점", 30, false))
            add(Crag(null, "더클라임 클라이밍 마곡점", 120, false))
            add(Crag(null, "더클라임 클라이밍 홍대점", 12, false))
            add(Crag(null, "더현대 홍대점", 126, false))
            add(Crag(null, "가톨릭대 홍대점", 126, false))

        }
    }


    private fun NavController.toClimbingGoal() {
        val action = FollowCragFragmentDirections.actionFollowCragFragmentToHowToKnowFragment3()
        navigate(action)
    }

}

//        val sortOptions = listOf("가나다순","팔로우순")

//        binding.spinner.adapter = CustomSpinnerAdaptor(requireActivity(), R.layout.item_spinner_sort_option, sortOptions)
//        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                val value = binding.spinner.getItemAtPosition(p2).toString()
//            }
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                // 선택되지 않은 경우
//            }
//        }