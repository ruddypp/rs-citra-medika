package com.example.ticketing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ticketing.data.TicketingDatabaseHelper
import com.example.ticketing.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: TicketingDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        dbHelper = TicketingDatabaseHelper(requireContext())
        
        // Setup poly dropdown
        setupPolyDropdown()
        
        // Set click listener for get queue number button
        binding.btnGetQueueNumber.setOnClickListener {
            if (validateForm()) {
                submitForm()
            }
        }
    }
    
    private fun setupPolyDropdown() {
        val polyItems = arrayOf(
            TicketingDatabaseHelper.POLY_GENERAL,
            TicketingDatabaseHelper.POLY_DENTAL,
            TicketingDatabaseHelper.POLY_KIA
        )
        
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, polyItems)
        binding.actvPoly.setAdapter(adapter)
    }
    
    private fun validateForm(): Boolean {
        var isValid = true
        
        // Validate NIK
        if (binding.etNik.text.toString().trim().isEmpty()) {
            binding.tilNik.error = "NIK tidak boleh kosong"
            isValid = false
        } else {
            binding.tilNik.error = null
        }
        
        // Validate Name
        if (binding.etName.text.toString().trim().isEmpty()) {
            binding.tilName.error = "Nama tidak boleh kosong"
            isValid = false
        } else {
            binding.tilName.error = null
        }
        
        // Validate Poly
        if (binding.actvPoly.text.toString().trim().isEmpty()) {
            binding.tilPoly.error = "Silahkan pilih poli"
            isValid = false
        } else {
            binding.tilPoly.error = null
        }
        
        // Validate Need
        if (binding.etNeed.text.toString().trim().isEmpty()) {
            binding.tilNeed.error = "Kebutuhan tidak boleh kosong"
            isValid = false
        } else {
            binding.tilNeed.error = null
        }
        
        return isValid
    }
    
    private fun submitForm() {
        val nik = binding.etNik.text.toString().trim()
        val name = binding.etName.text.toString().trim()
        val poly = binding.actvPoly.text.toString().trim()
        val need = binding.etNeed.text.toString().trim()
        
        try {
            val queueId = dbHelper.insertQueue(nik, name, poly, need)
            
            if (queueId > 0) {
                // Navigate to result screen with queue ID
                val action = RegistrationFragmentDirections.actionRegistrationFragmentToQueueResultFragment(queueId)
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Gagal membuat antrian", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 