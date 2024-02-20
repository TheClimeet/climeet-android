package com.climus.climeet.presentation.ui.main.shorts.bottomsheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentShortsCommentBottomSheetBinding
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsCommentAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShortsCommentBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentShortsCommentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val args: ShortsCommentBottomSheetFragmentArgs by navArgs()
    private val shortsId by lazy { args.shortsId }
    private val profileImgUrl by lazy { args.profileImg }

    private val viewModel: ShortsCommentBottomSheetViewModel by viewModels()

    private var adapter: ShortsCommentAdapter? = null
    private var inputMethodManager: InputMethodManager? = null

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_shorts_comment_bottom_sheet,
            container,
            false
        )
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        binding.vm = viewModel
        adapter = ShortsCommentAdapter()
        binding.rvComment.adapter = adapter
        viewModel.setShortsId(shortsId, profileImgUrl)
        initStateObserver()
        initEventObserver()
        recyclerViewListener()
        editTextListener()
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                adapter?.submitList(it.shortsCommentList)
            }
        }

        repeatOnStarted {
            viewModel.comment.collect {
                if (it.isNotBlank()) {
                    binding.btnAddComment.isEnabled = true
                    binding.btnAddComment.alpha = 1F
                } else {
                    binding.btnAddComment.isEnabled = false
                    binding.btnAddComment.alpha = 0.2F
                }
            }
        }
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is ShortsCommentBottomSheetEvent.AddCommentComplete -> {
                        binding.etComment.setText("")
                    }

                    is ShortsCommentBottomSheetEvent.ShowToastMessage -> {}
                    is ShortsCommentBottomSheetEvent.GoToPosition -> {
                        smoothScroller.targetPosition = it.position
                        binding.rvComment.layoutManager?.startSmoothScroll(smoothScroller)
                    }

                    is ShortsCommentBottomSheetEvent.StartAddSubComment -> focusEditText(it.nick)
                    is ShortsCommentBottomSheetEvent.SetMyProfile -> {
                        Glide.with(requireContext())
                            .load(it.url)
                            .into(binding.ivProfile)
                    }
                }
            }
        }
    }

    private fun recyclerViewListener() {
        binding.rvComment.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.getCommentList()
                }
            }
        })
    }

    private fun editTextListener() {

        binding.btnCancelAdding.setOnClickListener {
            viewModel.changeAddCommentType(AddCommentType.MAIN)
            binding.tvAddingSubComment.visibility = View.GONE
            binding.btnCancelAdding.visibility = View.GONE
            inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    private val smoothScroller: RecyclerView.SmoothScroller by lazy {
        object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference() = SNAP_TO_START
        }
    }

    private fun focusEditText(nick: String) {
        binding.tvAddingSubComment.text = "$nick 님에게 답글 남기는중..."
        binding.tvAddingSubComment.visibility = View.VISIBLE
        binding.btnCancelAdding.visibility = View.VISIBLE
        binding.etComment.requestFocus()
        inputMethodManager?.showSoftInput(binding.etComment, 0)
    }

}