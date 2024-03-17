package com.example.invoicegenerator.ui.estimate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.invoicegenerator.databinding.FragmentSelectClientBinding


class SelectClientFragment : Fragment() {

    // Direct reference to the components on the layout that have an ID.
    private var _binding: FragmentSelectClientBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        _binding = FragmentSelectClientBinding.inflate(inflater, container, false)


        // Return the root for this fragment.
        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupDynamicClientsLis()  // Display all clients on the list.
    }


    // Set the custom adapter Listener
    private fun setupClientAdapterAndListener(): SelectClientAdapter {
        return SelectClientAdapter(requireContext(), object : SelectClientAdapter.ClientListener {
            override fun onItemClick(id: Long) {
                navigateToPrevious(id) // Navigate to previous fragment
            }

        })
    }


    // Display client in dynamic list on the layout.
    private fun setupDynamicClientsLis() {

        _binding!!.rvSelectClient.layoutManager = LinearLayoutManager(requireContext())
        _binding!!.rvSelectClient.setHasFixedSize(true)
        _binding!!.rvSelectClient.adapter = setupClientAdapterAndListener()
    }


    // Navigate to the previous and pass the selected client id.
    private fun navigateToPrevious(id: Long) {
        val action = SelectClientFragmentDirections.actionSelectClientToNewEstimate(id)
        findNavController().navigate(action)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}