package com.example.ticketing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ticketing.data.TicketingDatabaseHelper
import com.example.ticketing.databinding.FragmentStaffDashboardBinding
import com.google.android.material.tabs.TabLayoutMediator

class StaffDashboardFragment : Fragment() {

    private var _binding: FragmentStaffDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: TicketingDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        dbHelper = TicketingDatabaseHelper(requireContext())
        
        setupViewPager()
    }
    
    private fun setupViewPager() {
        val pagerAdapter = PolyPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        
        // Connect TabLayout with ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.general_poly)
                1 -> getString(R.string.dental_poly)
                2 -> getString(R.string.kia_poly)
                else -> "Tab ${position + 1}"
            }
        }.attach()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    inner class PolyPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        
        override fun getItemCount(): Int = 3
        
        override fun createFragment(position: Int): Fragment {
            val polyType = when (position) {
                0 -> TicketingDatabaseHelper.POLY_GENERAL
                1 -> TicketingDatabaseHelper.POLY_DENTAL
                2 -> TicketingDatabaseHelper.POLY_KIA
                else -> TicketingDatabaseHelper.POLY_GENERAL
            }
            
            return PolyFragment.newInstance(polyType)
        }
    }
} 