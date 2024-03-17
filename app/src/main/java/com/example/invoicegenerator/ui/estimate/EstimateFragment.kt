package com.example.invoicegenerator.ui.estimate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.invoicegenerator.MainActivity
import com.example.invoicegenerator.R
import com.example.invoicegenerator.databinding.FragmentEstimateBinding
import com.example.utilityui.DynamicList
import com.google.android.material.floatingactionbutton.FloatingActionButton


class EstimateFragment : Fragment() {

    // Direct reference to the components on the layout that have an ID.
    private var _binding: FragmentEstimateBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        accessMainButton().bringToFront() // Bring button to the front

        // Inflate the layout for this fragment.
        _binding = FragmentEstimateBinding.inflate(inflater, container, false)

        // Return the root for this fragment.
        return _binding!!.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        accessMainButton().setOnClickListener {
            navToNewEstimate()
        }

        displayEstimatesOnLis()
    }


    // Set the custom adapter and handel clickListener.
    private fun getEstimateAdapterAndListener(): EstimateAdapter {
        return EstimateAdapter(requireContext(), object : EstimateAdapter.EstimateListener{
            override fun onItemClick(id: Long) {
                navToEstimateLine(id)
            }

        })
    }


    private fun displayEstimatesOnLis() {

        DynamicList.setupVertically( _binding!!.rvEstimate, requireContext()) // Prepare RecyclerView.
        _binding!!.rvEstimate.adapter = getEstimateAdapterAndListener()
    }


    // Access the button in the MainActivity.
    private fun accessMainButton(): FloatingActionButton {
        return (requireActivity() as MainActivity).fab
    }


    // Navigate to new estimate
    private fun navToNewEstimate() {
        findNavController().navigate(R.id.action_nav_estimate_to_new)
    }

    // Navigate to estimate line in order to show details.
    private fun navToEstimateLine(id: Long) {
        val action = EstimateFragmentDirections.actionNavEstimateToEstimateLine(id)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}