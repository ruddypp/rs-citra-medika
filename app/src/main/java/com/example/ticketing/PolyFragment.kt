package com.example.ticketing

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ticketing.adapter.QueueAdapter
import com.example.ticketing.databinding.FragmentPolyBinding
import com.example.ticketing.model.Queue
import com.example.ticketing.data.TicketingDatabaseHelper

class PolyFragment : Fragment(), QueueAdapter.QueueActionListener {

    private var _binding: FragmentPolyBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: TicketingDatabaseHelper
    private lateinit var adapter: QueueAdapter
    private var polyType: String = TicketingDatabaseHelper.POLY_GENERAL

    companion object {
        private const val ARG_POLY_TYPE = "poly_type"

        fun newInstance(polyType: String): PolyFragment {
            val fragment = PolyFragment()
            val args = Bundle().apply {
                putString(ARG_POLY_TYPE, polyType)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            polyType = it.getString(ARG_POLY_TYPE, TicketingDatabaseHelper.POLY_GENERAL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPolyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        dbHelper = TicketingDatabaseHelper(requireContext())
        
        setupRecyclerView()
        loadQueueData()
    }
    
    private fun setupRecyclerView() {
        adapter = QueueAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }
    
    private fun loadQueueData() {
        val cursor = dbHelper.getActiveQueuesForPoly(polyType)
        val queueList = mutableListOf<Queue>()
        
        while (cursor.moveToNext()) {
            queueList.add(cursorToQueue(cursor))
        }
        
        cursor.close()
        
        if (queueList.isEmpty()) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            adapter.submitList(queueList)
        }
    }
    
    private fun cursorToQueue(cursor: Cursor): Queue {
        val idIndex = cursor.getColumnIndexOrThrow(TicketingDatabaseHelper.COLUMN_ID)
        val nikIndex = cursor.getColumnIndexOrThrow(TicketingDatabaseHelper.COLUMN_NIK)
        val nameIndex = cursor.getColumnIndexOrThrow(TicketingDatabaseHelper.COLUMN_NAME)
        val polyIndex = cursor.getColumnIndexOrThrow(TicketingDatabaseHelper.COLUMN_POLY)
        val needIndex = cursor.getColumnIndexOrThrow(TicketingDatabaseHelper.COLUMN_NEED)
        val queueNumberIndex = cursor.getColumnIndexOrThrow(TicketingDatabaseHelper.COLUMN_QUEUE_NUMBER)
        val statusIndex = cursor.getColumnIndexOrThrow(TicketingDatabaseHelper.COLUMN_STATUS)
        val createdAtIndex = cursor.getColumnIndexOrThrow(TicketingDatabaseHelper.COLUMN_CREATED_AT)
        
        return Queue(
            id = cursor.getLong(idIndex),
            nik = cursor.getString(nikIndex),
            name = cursor.getString(nameIndex),
            poly = cursor.getString(polyIndex),
            need = cursor.getString(needIndex),
            queueNumber = cursor.getString(queueNumberIndex),
            status = cursor.getString(statusIndex),
            createdAt = cursor.getLong(createdAtIndex)
        )
    }
    
    override fun onCallClicked(queue: Queue) {
        dbHelper.updateQueueStatus(queue.id, TicketingDatabaseHelper.STATUS_CALLED)
        queue.status = TicketingDatabaseHelper.STATUS_CALLED
        adapter.notifyDataSetChanged()
    }
    
    override fun onDoneClicked(queue: Queue) {
        dbHelper.updateQueueStatus(queue.id, TicketingDatabaseHelper.STATUS_DONE)
        loadQueueData() // Reload data to remove the done item
    }
    
    override fun onResume() {
        super.onResume()
        loadQueueData() // Reload data when fragment becomes visible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 