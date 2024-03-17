package com.example.invoicegenerator.ui.client

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.invoicegenerator.MainActivity
import com.example.invoicegenerator.R
import com.example.invoicegenerator.database.InvGContract.ClientEntry
import com.example.invoicegenerator.database.InvGContract.TablesEntry
import com.example.invoicegenerator.database.InvGDB
import com.example.invoicegenerator.databinding.FragmentUpdateClientBinding
import com.example.utilityui.InputValidation
import com.example.utilityui.Utility
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UpdateClientFragment : Fragment() {


    // Direct reference to the components on the layout that have an ID.
    private var _binding: FragmentUpdateClientBinding? = null

    // Receive date coning from previous fragment
    private val _args: UpdateClientFragmentArgs by navArgs()

    // Database.
    private var _db: InvGDB? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Config mainButton
        configFab(View.GONE)

        // Inflate the layout for this fragment.
        _binding = FragmentUpdateClientBinding.inflate(inflater, container, false)

        // Return root.
        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loadClientDateOnViews()
        handelToolBar()
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
                menuInflater.inflate(R.menu.menu_update, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.menu_update -> {

                        // Database
                        _db = InvGDB(requireContext())
                        // Save new client to database.
                        if (!InputValidation.isEmpty(_binding!!.incUpdateClient.tfClientName.editText))
                            if (_db!!.updateClient(getClientData(), _args.id)) {
                                Toast.makeText(context, "Info updated", Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            }


                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    // Load client data.
    private fun holdData() : Cursor {

        _db = InvGDB(requireContext())
        return _db!!.select(TablesEntry.CLIENT, "_id", _args.id)
    }


    // Set the old data on views on the layout.
    private fun loadClientDateOnViews() {

        val cursor = holdData()

        if (cursor.moveToFirst()) {

            _binding!!.incUpdateClient.name.setText(Utility.getColumnDataAsString(cursor, ClientEntry.COLUMN_NAME))
            _binding!!.incUpdateClient.phone.setText(Utility.getColumnDataAsString(cursor, ClientEntry.COLUMN_PHONE))
            _binding!!.incUpdateClient.email.setText(Utility.getColumnDataAsString(cursor, ClientEntry.COLUMN_EMAIL))
        }
    }


    // Access the button in the MainActivity.
    private fun accessMainButton(): FloatingActionButton {
        return (requireActivity() as MainActivity).fab
    }


    // Get data from the user
    private fun getClientData(): Client {

        val name = InputValidation.text(_binding!!.incUpdateClient.tfClientName.editText)
        val phone = InputValidation.text(_binding!!.incUpdateClient.tfClientPhone.editText, true)
        val email = InputValidation.text(_binding!!.incUpdateClient.tfClientEmail.editText, true)

        return Client(name, phone, email)
    }


    /**
     *  Set source icon on the button.
     * @param visibility the visibility state of the main button.
     */
    private fun configFab(visibility: Int) {
        accessMainButton().visibility = visibility
    }


    override fun onDestroy() {
        configFab(View.VISIBLE)
        super.onDestroy()
    }
}