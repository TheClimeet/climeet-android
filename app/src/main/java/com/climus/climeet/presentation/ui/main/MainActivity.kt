package com.climus.climeet.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.ActivityMainBinding
import com.climus.climeet.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBnv()
    }

    private fun setBnv(){

        binding.mainBnv.itemIconTintList = null

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.mainBnv.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _,destination,_ ->
            if(destination.id == R.id.home_fragment || destination.id == R.id.shorts_fragment || destination.id == R.id.upload_fragment
                || destination.id == R.id.record_fragment || destination.id == R.id.myPage_fragment
                || destination.id == R.id.bestClimerFragment || destination.id == R.id.popularShortsFragment
                || destination.id == R.id.popularCragsFragment || destination.id == R.id.popularRoutesFragment
                || destination.id == R.id.searchCragFragment){
                // todo bnv show 해야되는 frag
                binding.mainBnv.visibility = View.VISIBLE
            }else{
                binding.mainBnv.visibility = View.INVISIBLE
            }

        }
    }
}