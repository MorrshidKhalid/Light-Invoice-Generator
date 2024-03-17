package com.example.invoicegenerator.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.invoicegenerator.MainActivity
import com.example.invoicegenerator.R
import com.example.invoicegenerator.databinding.FragmentClientBinding
import com.example.utilityui.DynamicList
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClientFragment : Fragment() {


    // Direct reference to the components on the layout that have an ID.
     private var _binding: FragmentClientBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        accessMainButton().bringToFront() // Bring the main button to the front of everything.

        // Inflate the layout for this fragment.
        _binding = FragmentClientBinding.inflate(inflater, container, false)
        return _binding!!.root // Root view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        displayClientsOnList()

        // Add new client.
        accessMainButton().setOnClickListener {
            navigateToNewClient()
        }
    }


    // Access the button in the MainActivity.
    private fun accessMainButton(): FloatingActionButton {
        return (requireActivity() as MainActivity).fab
    }


    // Navigate to (New client fragment).
    private fun navigateToNewClient() {
        findNavController().navigate(R.id.action_client_to_new)
    }


    /**
     * Navigate to (Update client fragment).
     * @param id Client Primary-Key.
     */
    private fun navigateToUpdateClient(id: Long) {

        val action = ClientFragmentDirections.actionClientToUpdate(id) // Data to pass it into the next directions.
        findNavController().navigate(action) // Navigate to update client fragment.
    }


    // Set the custom adapter and handel clickListener.
    private fun getClientAdapterAndListener(): ClientAdapter {
        return ClientAdapter(requireContext(), object : ClientAdapter.ClientListener {
            override fun onItemClick(id: Long) {
                navigateToUpdateClient(id)
            }
        })
    }


    private fun displayClientsOnList() {

        DynamicList.setupVertically(_binding!!.rvClient, requireContext()) // Prepare RecyclerView.
        _binding!!.rvClient.adapter = getClientAdapterAndListener() // Set the client adapter.
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}