package com.example.invoicegenerator.ui.estimate_line

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.example.invoicegenerator.MainActivity
import com.example.invoicegenerator.R
import com.example.invoicegenerator.databinding.FragmentEstimateLineBinding
import com.example.utilityui.DynamicList
import com.google.android.material.floatingactionbutton.FloatingActionButton


class EstimateLineFragment : Fragment() {

    // Direct reference to the components on the layout that have an ID.
    private var _binding: FragmentEstimateLineBinding? = null
    private val _args: EstimateLineFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        configFab(View.GONE)
        // Inflate the layout for this fragment.
        _binding = FragmentEstimateLineBinding.inflate(inflater, container, false)

        // Return the root for this fragment.
        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        displayEstimatesLineOnLis() // Display all estimate details.

        handelToolBar()
    }


    // Access the button in the MainActivity.
    private fun accessMainButton(): FloatingActionButton {
        return (requireActivity() as MainActivity).fab
    }


    /**
     *  Set source icon on the button.
     * @param visibility the visibility state of the main button.
     */
    private fun configFab(visibility: Int) {
        accessMainButton().visibility = visibility
    }



    // Handel toolbar
    private fun handelToolBar() {

        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_shate_download_serch, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.menu_search -> {

                        Toast.makeText(context, "search", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.menu_pdf -> {

                        Toast.makeText(context, "pdf", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.menu_share -> {

                        Toast.makeText(context, "share", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    // Set the custom adapter and handel clickListener.
    private fun setupEstimateLineAdapterAndListener(): EstimateLineAdapter {
        return EstimateLineAdapter(requireContext(), _args.id)
    }


    // Display client in dynamic list on the layout.
    private fun displayEstimatesLineOnLis() {

        DynamicList.setupVertically( _binding!!.rvEstimateLine, requireContext()) // Prepare RecyclerView.
        _binding!!.rvEstimateLine.adapter = setupEstimateLineAdapterAndListener()
    }


    override fun onDestroy() {
        super.onDestroy()
        configFab(View.VISIBLE)
        _binding = null
    }
}