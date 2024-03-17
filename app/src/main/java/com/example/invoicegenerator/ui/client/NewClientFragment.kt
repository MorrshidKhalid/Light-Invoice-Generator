package com.example.invoicegenerator.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.invoicegenerator.MainActivity
import com.example.invoicegenerator.R
import com.example.invoicegenerator.database.InvGDB
import com.example.invoicegenerator.databinding.FragmentNewClientBinding
import com.example.utilityui.InputValidation
import com.google.android.material.floatingactionbutton.FloatingActionButton


class NewClientFragment : Fragment() {

    // Direct reference to the components on the layout that have an ID.
    private var _binding: FragmentNewClientBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Config mainButton
        configFab(View.GONE)


        // Inflate the layout for this fragment.
        _binding = FragmentNewClientBinding.inflate(inflater, container, false)

        // Return the root for this fragment.
        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        handelToolBar() // Declare a menu and handel save
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

                        // Database
                        val db = InvGDB(requireContext())
                        // Save new client to database.
                        if (!InputValidation.isEmpty(_binding!!.incNewClient.tfClientName.editText))
                            if (db.addOneClient(getUserInput()))
                                findNavController().popBackStack()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    // Access the button in the MainActivity.
    private fun accessMainButton(): FloatingActionButton {
        return (requireActivity() as MainActivity).fab
    }


    /**
     *
     *  Config the visibility of a floating action button.
     * @param visibility the visibility state of the main button.
     */
    private fun configFab(visibility: Int) {
        accessMainButton().visibility = visibility
    }


    // Get input from the user
    private fun getUserInput(): Client {

        // Initial the client object values.
        val name = InputValidation.text(_binding!!.incNewClient.tfClientName.editText)
        val phone = InputValidation.text(_binding!!.incNewClient.tfClientPhone.editText, true)
        val email = InputValidation.text(_binding!!.incNewClient.tfClientEmail.editText, true)

        // Return a one client object
        return Client(name, phone, email)
    }


    override fun onDestroy() {
        super.onDestroy()
        configFab(View.VISIBLE)
        _binding = null
    }
}