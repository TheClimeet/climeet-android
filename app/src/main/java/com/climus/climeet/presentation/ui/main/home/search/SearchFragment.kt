package com.climus.climeet.presentation.ui.main.home.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.data.model.response.BestRouteDetailInfoResponse
import com.climus.climeet.data.model.response.UserFollowSimpleResponse
import com.climus.climeet.databinding.FragmentSearchBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.adapter.FollowCragRVAdapter
import com.climus.climeet.presentation.ui.main.home.recycler.following.FollowingSearchRVAdapter
import com.climus.climeet.presentation.ui.main.home.search.model.FollowClimber
import com.climus.climeet.presentation.ui.main.home.search.viewpager.SearchVPAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val viewModel: SearchCragViewModel by viewModels()
    private var recyclerClimber: List<FollowClimber> = emptyList()
    private var cragAdapter: FollowCragRVAdapter? = null
    private var followingAdapter: FollowingSearchRVAdapter? = null
    private var isCrag: Boolean = true
    lateinit var editText: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        cragAdapter = FollowCragRVAdapter()
        followingAdapter = FollowingSearchRVAdapter()
        //binding.rvFollowSearchCrags.adapter = cragAdapter
        //binding.rvSearchFollowing.adapter = followingAdapter
        viewModel.getRouteRankingOrderSelectionCount()
        viewModel.getClimberFollowing()

        initStateObserve()
        setupSearchTask()
        setupSearchCragTask()
        setupOnClickListener()

    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel?.let { vm ->
                vm.uiState.collect { uiState ->
                    uiState.routeList?.let { routeList ->
                        recyclerRoute = routeList
                        //setupPopularRoutes()
                    }

                    uiState.followingList?.let { followingList ->
                        recyclerFollowing = followingList
                        //setupFollowingList()
                    }

                    recyclerClimber = uiState.searchClimberList
                    Log.d("Follow", recyclerClimber.toString())
                    cragAdapter?.setList(uiState.searchList, viewModel.keyword.value)
                    followingAdapter?.setList(uiState.searchClimberList, viewModel.keyword.value)

                }
            }

        }

    }

    private fun setupSearchTask() {
        val searchAdapter = SearchVPAdapter(this)
        binding.vpSearchCragClimber.adapter = searchAdapter
        binding.vpSearchCragClimber.isUserInputEnabled = false
        val tabMenu = arrayListOf(" 암장 ", " 클라이머 ")
        TabLayoutMediator(binding.tbSearchCragClimber, binding.vpSearchCragClimber) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()
    }

    private fun setupSearchCragTask() {
        isCrag = true
        editText = binding.etSearchCrag
        editText.text.clear()
        editText.hint = "암장 검색하기"

        //binding.clSerachCrags.visibility = View.VISIBLE
        //binding.clSerachCrags2.visibility = View.INVISIBLE
        //binding.rvSearchFollowing.visibility = View.INVISIBLE
        binding.linearCrag.visibility = View.VISIBLE

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                //binding.clSerachCrags.visibility = if (charSequence.isNullOrEmpty() && isCrag) View.VISIBLE else View.INVISIBLE
                //binding.rvFollowSearchCrags.visibility = if (charSequence.isNullOrEmpty() && isCrag) View.INVISIBLE else View.VISIBLE
                //binding.rvFollowCrags.visibility = if (charSequence.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

//        if (binding.clSerachCrags.visibility == View.VISIBLE) {
//            binding.icEmptySearch.visibility = View.INVISIBLE
//            binding.tvEmptySearch.visibility = View.INVISIBLE
//            binding.tvSearchCragFollowing.visibility = View.INVISIBLE
//        }
    }

    private fun setupSearchClimberTask() {
        isCrag = false
        editText = binding.etSearchCrag
        editText.text.clear()
        editText.hint = "클라이머 검색하기"
//        binding.clSerachCrags2.visibility = View.VISIBLE
//        binding.clSerachCrags.visibility = View.INVISIBLE
//        binding.rvFollowSearchCrags.visibility = View.INVISIBLE
//
//        binding.icEmptySearch.visibility = View.VISIBLE
//        binding.tvEmptySearch.visibility = View.VISIBLE
//        binding.tvSearchCragFollowing.visibility = View.VISIBLE
//        binding.rvSearchFollowing.visibility = View.VISIBLE

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
//                binding.clSerachCrags2.visibility = if (charSequence.isNullOrEmpty() && !isCrag) View.VISIBLE else View.INVISIBLE
//                binding.rvSearchFollowing.visibility = if (charSequence.isNullOrEmpty()&& !isCrag) View.INVISIBLE else View.VISIBLE
//                binding.rvFollowSearchCrags.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

//        if (binding.clSerachCrags2.visibility == View.VISIBLE) {
//            binding.icEmptySearch.visibility = View.INVISIBLE
//            binding.tvEmptySearch.visibility = View.INVISIBLE
//            binding.rvSearchFollowing.visibility = View.VISIBLE
//        }

    }

    private fun setupOnClickListener() {

        binding.icPopularRoutesBack.setOnClickListener {
            findNavController().navigateUp()
        }

//        binding.tvSearchMenuCrag.setOnClickListener {
//            binding.tvSearchMenuCrag.setBackgroundResource(R.drawable.rect_greyfill_nostroke_10radius)
//            binding.tvSearchMenuCrag.setTextColor(Color.WHITE)
//            binding.tvSearchMenuClimer.setBackgroundResource(R.drawable.rect_blackfill_nostroke_10radius)
//            val color: Int = Color.parseColor("#B3B3B3")
//            binding.tvSearchMenuClimer.setTextColor(color)
//            setupSearchCragTask()
//        }
//
//        binding.tvSearchMenuClimer.setOnClickListener {
//            binding.tvSearchMenuClimer.setBackgroundResource(R.drawable.rect_greyfill_nostroke_10radius)
//            binding.tvSearchMenuClimer.setTextColor(Color.WHITE)
//            binding.tvSearchMenuCrag.setBackgroundResource(R.drawable.rect_blackfill_nostroke_10radius)
//            val color: Int = Color.parseColor("#B3B3B3")
//            binding.tvSearchMenuCrag.setTextColor(color)
//            setupSearchClimberTask()
//        }
    }

//    private fun setupPopularRoutes() {
//        val popularRouteRVAdapter = PopularRouteRVAdapter(recyclerRoute)
//        setupRecyclerView(binding.rvSearchCragRoutes1, popularRouteRVAdapter, LinearLayoutManager.HORIZONTAL)
//        setupRecyclerView(binding.rvSearchCragRoutes2, popularRouteRVAdapter, LinearLayoutManager.HORIZONTAL)
//    }
//
//    private fun setupFollowingList() {
//        val followingRVAdapter = FollowingRVAdapter(recyclerFollowing)
//        setupRecyclerView(binding.rvSearchFollowing, followingRVAdapter, LinearLayoutManager.VERTICAL)
//    }
//
//    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, orientation: Int) {
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), orientation, false)
//    }

}