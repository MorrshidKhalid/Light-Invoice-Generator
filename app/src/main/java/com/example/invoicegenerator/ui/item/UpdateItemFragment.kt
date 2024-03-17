package com.example.invoicegenerator.ui.item

import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.invoicegenerator.MainActivity
import com.example.invoicegenerator.R
import com.example.invoicegenerator.database.InvGContract.ItemEntry
import com.example.invoicegenerator.database.InvGContract.TablesEntry
import com.example.invoicegenerator.database.InvGDB
import com.example.invoicegenerator.databinding.FragmentUpdateItemBinding
import com.example.utilityui.InputValidation
import com.example.utilityui.Utility
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UpdateItemFragment : Fragment() {


    // Direct reference to the components on the layout that have an ID.
    private var _binding: FragmentUpdateItemBinding? = null

    // Receive date coning from previous fragment
    private val _args: UpdateItemFragmentArgs by navArgs()

    // Database.
    private var _db: InvGDB? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Config mainButton
        configFab(View.GONE)

        // Inflate the layout for this fragment.
        _binding = FragmentUpdateItemBinding.inflate(inflater, container, false)


        // Return the root for this fragment.
        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loadClientDateOnViews()
        handelToolBar()
        loadDataToSpinnerUI()
        autoCompleteTextViewSetAdapter()

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
                        if (!isItemFieldsEmpty())
                            if (_db!!.updateItem(getUserInput(), _args.id)) {
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
    private fun holdClientData(): Cursor {
        _db = InvGDB(requireContext())
        return _db!!.select(TablesEntry.ITEM, "_id", _args.id)
    }


    // Set the old data on views on this layout.
    private fun loadClientDateOnViews() {

        val cursor = holdClientData()

        if (cursor.moveToFirst()) {
            _binding!!.incItem.name.setText(Utility.getColumnDataAsString(cursor, ItemEntry.COLUMN_NAME))
            _binding!!.incItem.autoTextUom.hint = Utility.getColumnDataAsString(cursor, ItemEntry.COLUMN_UOM)
            _binding!!.incItem.des.setText(Utility.getColumnDataAsString(cursor, ItemEntry.COLUMN_DESCRIPTION))
            _binding!!.incItem.cost.setText(Utility.getColumnDataAsString(cursor, ItemEntry.COLUMN_BASE_PRICE))
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


    // Set the selected user currency
    private fun setSelectedCurrency(): String {

        val spinner: Spinner = _binding!!.incItem.currency
        return spinner.selectedItem.toString()
    }


    // Get the item date from the user
    private fun getUserInput(): Item {

        // Initial the client object values.
        val name = InputValidation.text(_binding!!.incItem.tfItemName.editText)
        val uom = InputValidation.text(_binding!!.incItem.autoTextUom)
        val des = InputValidation.text(_binding!!.incItem.tfDes.editText, true)
        val basePrice = InputValidation.text(_binding!!.incItem.tfBaseCost.editText)

        // Return a one item object
        return Item(name, uom, des, setSelectedCurrency(), basePrice.toFloat())
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


    // Check if the necessary field are not empty.
    private fun isItemFieldsEmpty(): Boolean {

        return InputValidation.isEmpty(_binding!!.incItem.tfItemName.editText) ||
//               Input.isEmpty(_binding!!.incItem.tfUOM.editText) ||
               InputValidation.isEmpty(_binding!!.incItem.tfBaseCost.editText)
    }


    /**
     *  Set source icon on the button.
     * @param visibility the visibility state of the main button.
     */
    private fun configFab(visibility: Int) {
        accessMainButton().visibility = visibility
    }


    // Access the button in the MainActivity.
    private fun accessMainButton(): FloatingActionButton {
        return (requireActivity() as MainActivity).fab
    }


    override fun onDestroy() {
        super.onDestroy()
        configFab(View.VISIBLE)
        _binding = null
    }
}