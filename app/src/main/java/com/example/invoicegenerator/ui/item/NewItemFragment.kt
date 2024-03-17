package com.example.invoicegenerator.ui.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.invoicegenerator.MainActivity
import com.example.invoicegenerator.R
import com.example.invoicegenerator.database.InvGDB
import com.example.invoicegenerator.databinding.FragmentNewItemBinding
import com.example.utilityui.InputValidation
import com.google.android.material.floatingactionbutton.FloatingActionButton


class NewItemFragment : Fragment() {

    // Direct reference to the components on the layout that have an ID.
    private var _binding: FragmentNewItemBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Config mainButton
        configFab(View.GONE)

        // Inflate the layout for this fragment
        _binding = FragmentNewItemBinding.inflate(inflater, container, false)

        // Return the root for this fragment.
        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Declare a menu and handel save.
        handelToolBar()
        loadDataToSpinnerUI()
        setCurrencySymbol() // helper text form the user.
        autoCompleteTextViewSetAdapter()



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
                menuInflater.inflate(R.menu.menu_save, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.menu_save -> {

                        // Database
                        val db = InvGDB(requireContext())
                        // Save new item to database.
                        if (!isItemInfoEmpty())
                            if (db.addOneItem(getUserInput()))
                                findNavController().popBackStack()

                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    // Check item info is not empty
    private fun isItemInfoEmpty(): Boolean {

        return InputValidation.isEmpty(_binding!!.incItem.tfItemName.editText) ||
               InputValidation.isEmpty(_binding!!.incItem.autoTextUom) ||
               InputValidation.isEmpty(_binding!!.incItem.tfBaseCost.editText)
    }


    // Get input from the user
    private fun getUserInput(): Item {

        // Initial the client object values.
        val name = InputValidation.text(_binding!!.incItem.tfItemName.editText)
        val uom = InputValidation.text(_binding!!.incItem.autoTextUom)
        val des = InputValidation.text(_binding!!.incItem.tfDes.editText, true)
        val basePrice = InputValidation.text(_binding!!.incItem.tfBaseCost.editText)

        // Return a one item object
        return Item(name, uom, des, setSelectedCurrency(), basePrice.toFloat())
    }


    // Set the selected user currency
    private fun setSelectedCurrency(): String {

        val spinner: Spinner = _binding!!.incItem.currency
        return spinner.selectedItem.toString()
    }


    // Load date to autoCompleteTextView
    private fun autoCompleteTextViewSetAdapter() {

        val array = resources.getStringArray(R.array.uom)
        InputValidation.setAutoTextAdapter(
            _binding!!.incItem.autoTextUom,
            array,
            requireContext()
        )
    }


    // Set the symbol of the currency to the helper text,
    private fun setCurrencySymbol() {

        val spinner: Spinner = _binding!!.incItem.currency
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                _binding!!.incItem.tfBaseCost.helperText = "symbol ${setSelectedCurrency().substring(0, 1)}"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }


    // Provide spinner UI with data.
    private fun loadDataToSpinnerUI() {

        val spinner: Spinner = _binding!!.incItem.currency

    // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currency_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        configFab(View.VISIBLE)
        _binding = null
    }
}