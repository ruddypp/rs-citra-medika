package com.example.ticketing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ticketing.data.TicketingDatabaseHelper
import com.example.ticketing.databinding.FragmentStaffLoginBinding

class StaffLoginFragment : Fragment() {

    private var _binding: FragmentStaffLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: TicketingDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        dbHelper = TicketingDatabaseHelper(requireContext())
        
        binding.btnLogin.setOnClickListener {
            validateAndLogin()
        }
    }
    
    private fun validateAndLogin() {
        val pin = binding.etPin.text.toString().trim()
        
        if (pin.isEmpty()) {
            binding.tilPin.error = "PIN tidak boleh kosong"
            return
        }
        
        if (dbHelper.verifyStaffPin(pin)) {
            // PIN correct, navigate to dashboard
            findNavController().navigate(R.id.action_staffLoginFragment_to_staffDashboardFragment)
        } else {
            // PIN incorrect, show error
            binding.tvPinError.visibility = View.VISIBLE
            binding.etPin.setText("")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 