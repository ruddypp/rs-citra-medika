package com.example.ticketing

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ticketing.data.TicketingDatabaseHelper
import com.example.ticketing.databinding.FragmentQueueResultBinding

class QueueResultFragment : Fragment() {

    private var _binding: FragmentQueueResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: TicketingDatabaseHelper
    private val args: QueueResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQueueResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        dbHelper = TicketingDatabaseHelper(requireContext())
        
        // Load queue data
        loadQueueData()
        
        // Set click listeners for buttons
        binding.btnGetNumberAgain.setOnClickListener {
            findNavController().navigate(R.id.action_queueResultFragment_to_registrationFragment)
        }
        
        binding.btnFinish.setOnClickListener {
            findNavController().navigate(R.id.action_queueResultFragment_to_welcomeFragment)
        }
    }
    
    private fun loadQueueData() {
        val queueId = args.queueId
        val cursor = dbHelper.getQueueById(queueId)
        
        if (cursor.moveToFirst()) {
            displayQueueData(cursor)
        } else {
            Toast.makeText(requireContext(), "Data antrian tidak ditemukan", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
        
        cursor.close()
    }
    
    private fun displayQueueData(cursor: Cursor) {
        val idxQueueNumber = cursor.getColumnIndexOrThrow(TicketingDatabaseHelper.COLUMN_QUEUE_NUMBER)
        val idxName = cursor.getColumnIndexOrThrow(TicketingDatabaseHelper.COLUMN_NAME)
        val idxPoly = cursor.getColumnIndexOrThrow(TicketingDatabaseHelper.COLUMN_POLY)
        val idxNeed = cursor.getColumnIndexOrThrow(TicketingDatabaseHelper.COLUMN_NEED)
        
        val queueNumber = cursor.getString(idxQueueNumber)
        val name = cursor.getString(idxName)
        val poly = cursor.getString(idxPoly)
        val need = cursor.getString(idxNeed)
        
        binding.tvQueueNumber.text = queueNumber
        binding.tvPatientName.text = name
        binding.tvPolyName.text = poly
        binding.tvNeed.text = need
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 