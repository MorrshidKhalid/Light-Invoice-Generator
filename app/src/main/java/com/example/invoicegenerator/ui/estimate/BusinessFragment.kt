package com.example.invoicegenerator.ui.estimate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.invoicegenerator.R
import com.example.invoicegenerator.databinding.FragmentBusinessBinding
import com.example.utilityui.InputValidation


class BusinessFragment : Fragment() {

    private var _binding: FragmentBusinessBinding? = null // Direct reference to the components on the layout that have an ID.
    private val sharedViewModel: FromToViewModel by activityViewModels() // Keep track data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessBinding.inflate(inflater, container, false) // Inflate the layout for this fragment
        return _binding!!.root // Return root.
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handelToolBar() // Handel save click from the user
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
                menuInflater.inflate(R.menu.menu_save, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.menu_save -> {

                        if (!InputValidation.isEmpty(_binding!!.tfBName.editText)) {
                            sharedViewModel.setBusiness(InputValidation.text(_binding!!.tfBName.editText))
                            findNavController().popBackStack()
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}