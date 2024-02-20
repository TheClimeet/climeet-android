package com.climus.climeet.presentation.ui.main.global.searchprofile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSearchProfileBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.adapter.FollowCragRVAdapter
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.FollowClimber
import com.climus.climeet.presentation.ui.main.global.searchprofile.viewpager.SearchVPAdapter
import com.climus.climeet.presentation.ui.main.home.recycler.following.FollowingSearchRVAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchProfileFragment : BaseFragment<FragmentSearchProfileBinding>(R.layout.fragment_search_profile) {

    private val viewModel: SearchProfileViewModel by viewModels()
    private var recyclerClimber: List<FollowClimber> = emptyList()
    private var recyclerCrag: List<FollowCrag> = emptyList()
    private var cragAdapter: FollowCragRVAdapter? = null
    private var followingAdapter: FollowingSearchRVAdapter? = null
    lateinit var editText: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        cragAdapter = FollowCragRVAdapter()
        followingAdapter = FollowingSearchRVAdapter()
        binding.rvSearchCrag.adapter = cragAdapter
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
                    cragAdapter?.setList(uiState.searchList, viewModel.keyword.value)
                    recyclerCrag = uiState.searchList
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
        editText = binding.etSearchCrag

        //binding.clSerachCrags2.visibility = View.INVISIBLE
        //binding.rvSearchFollowing.visibility = View.INVISIBLE

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clSerachCrag.visibility = if (charSequence.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE

            }

            override fun afterTextChanged(editable: Editable?) {}
        })

//        if (binding.clSerachCrag.visibility == View.VISIBLE) {
//            binding.icEmptySearch.visibility = View.VISIBLE
//            binding.tvEmptySearch.visibility = View.VISIBLE
//
//        } else {
//            binding.icEmptySearch.visibility = View.INVISIBLE
//            binding.tvEmptySearch.visibility = View.INVISIBLE
//        }
    }

    private fun setupSearchClimberTask() {
        editText = binding.etSearchCrag
        editText.text.clear()
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