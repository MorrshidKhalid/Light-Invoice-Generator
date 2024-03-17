package com.example.invoicegenerator.ui.estimate

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.invoicegenerator.MainActivity
import com.example.invoicegenerator.R
import com.example.invoicegenerator.database.InvGContract.TablesEntry
import com.example.invoicegenerator.database.InvGContract.EstimateEntry
import com.example.invoicegenerator.database.InvGContract.ItemEntry
import com.example.invoicegenerator.database.InvGContract.ClientEntry
import com.example.invoicegenerator.database.InvGDB
import com.example.invoicegenerator.databinding.FragmentNewEstimateBinding
import com.example.invoicegenerator.ui.client.UpdateClientFragmentArgs
import com.example.utilityui.InputValidation
import com.example.utilityui.Utility
import com.google.android.material.floatingactionbutton.FloatingActionButton


class NewEstimateFragment : Fragment() {

    private var _binding: FragmentNewEstimateBinding? = null // Direct reference to the components on the layout that have an ID.
    private val _sharedViewModel: FromToViewModel by activityViewModels() // Keep track business data.
    private val _args: UpdateClientFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        configFab(View.GONE) // Config mainButton

        _binding = FragmentNewEstimateBinding.inflate(inflater, container, false) // Inflate the layout for this fragment
        return _binding!!.root // Return root.
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        handelSave() // Handel save button on the toolbar

        navigate() // Navigate base on the selection of the user.

        setupTextToUI() // Set default values on textView UI.

        // Add new dynamic view.
        _binding!!.listItem.btnAdd.setOnClickListener {
            addView()
        }


    }


    // Handel toolbar
    private fun handelSave() {

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

                        val db = InvGDB(requireContext())

                        _sharedViewModel.business.observe(viewLifecycleOwner) { business ->
                            val estimate = Estimate(Utility.currentDate(), _args.id, 0, "nope", business)
                            if (db.addOneEstimate(estimate)) {
                                // Update the estimate total cost.
                                db.updateRow(TablesEntry.ESTIMATE, EstimateEntry.COLUMN_TOTAL, getTotalCost(db), db.lastID(TablesEntry.ESTIMATE))

                                findNavController().popBackStack() // Go back to previous fragment.
                            }
                        }

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
     *  Set source icon on the button.
     * @param visibility the visibility state of the main button.
     */
    private fun configFab(visibility: Int) {
        accessMainButton().visibility = visibility
    }



    private fun onCreateDynamicView(): View {

        // Inflate the dynamic view.
        val inflater = LayoutInflater.from(requireContext())
            .inflate(R.layout.row_dynamic_list, _binding!!.root, false)

        // Find components on the dynamic view.
        val spItem = inflater.findViewById<Spinner>(R.id.spItem)
        val tvUom = inflater.findViewById<TextView>(R.id.tv_uom)
        val tvDes = inflater.findViewById<TextView>(R.id.tv_des)



        loadDataToSpinner(spItem) // Load data to the spinner for this dynamic view.
        loadDataToUIOnItemSelected(spItem, tvUom, tvDes) // Set item details on components for this dynamic view.



        rmView(inflater) // Remove one DynamicView

        return inflater
    }



    // Setup text to UI components on the layout.
    private fun setupTextToUI() {

        _binding!!.incDate.tvDate.text = Utility.currentDate() // Get the system current date.

        // Setup client and business text title to UI.
        _binding!!.incFromTo.incFrom.tvTitle.text = getText(R.string.tv_from)
        _binding!!.incFromTo.incTo.tvTitle.text = getText(R.string.tv_to)


        _binding!!.incFromTo.incTo.tvContent.text = getSelectedClient() // Set the selected client text value.

        // Set the selected business text value.
        _sharedViewModel.business.observe(viewLifecycleOwner) { business ->
            _binding!!.incFromTo.incFrom.tvContent.text = business
        }
    }


    // Navigate to other destinations From business or To client.
    private fun navigate() {

        _binding!!.incFromTo.incFrom.parentFromTo.setOnClickListener {
            findNavController().navigate(R.id.action_newEstimate_to_business)
        }
        _binding!!.incFromTo.incTo.parentFromTo.setOnClickListener {
            findNavController().navigate(R.id.action_newEstimateFragment_to_select_client)
        }
    }


    private fun getSelectedClient(): String {

        if (_args.id.toInt() == -1)
            return getString(R.string.tv_to_content)

        val cursor = holdClientData()
        var name = ""
        if (cursor.moveToFirst())
            name = Utility.getColumnDataAsString(cursor, ClientEntry.COLUMN_NAME)

        return name
    }


    // Hold client name
    private fun holdClientData(): Cursor {

        val db = InvGDB(requireContext())
        return db.select(TablesEntry.CLIENT, "_id", _args.id)
    }


    // Add one dynamic view on the layout.
    private fun addView() {
        _binding!!.listItem.listView.addView(onCreateDynamicView())
    }


    // Remove a dynamic views.
    private fun rmView(view: View) {

        val rm: FloatingActionButton = view.findViewById(R.id.rmView)
        rm.setOnClickListener {
            _binding!!.listItem.listView.removeView(view)
        }
    }


    // Hold item data coming from the database.
    private fun holdItemsData(): Cursor {

        val db = InvGDB(requireContext())
        return db.select(TablesEntry.ITEM)
    }


    // Load item data for spinner in the dynamic view.
    private fun loadDataToSpinner(spinner: Spinner) {

        val adapter = SimpleCursorAdapter(
            requireContext(),
            R.layout.spinner,
            holdItemsData(), arrayOf(ItemEntry.COLUMN_NAME), intArrayOf(R.id.tv_sp), 0
        )

        spinner.adapter = adapter
    }


    private fun loadDataToUIOnItemSelected(spinner: Spinner, uom: TextView, des: TextView) {

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                uom.text = clmData(id, ItemEntry.COLUMN_NAME)
                des.text = clmData(id, ItemEntry.COLUMN_DESCRIPTION)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }


    // Get the column value.
    private fun clmData(id: Long, clm: String): String {

        val cursor = InvGDB(requireContext()).select(TablesEntry.ITEM, "_id", id)
        var str = ""
        if (cursor.moveToFirst())
            str = Utility.getColumnDataAsString(cursor, clm)

        return str
    }


    // Provide the total estimate cost.
    private fun getTotalCost(db: InvGDB): Int {

        var childView: View     // The child views.
        var edtPrice: EditText //  Inputs components on the layout for price per one item.
        var edtQty: EditText  //   Inputs components on the layout for quantity.
        var spItem: Spinner  //    Spinner on the layout.
        var total = 0 // Total estimate cost.


        if (_binding!!.listItem.listView.childCount <= 0)
            return -1

        for (at in 0u until _binding!!.listItem.listView.childCount.toUInt()) {

            childView = _binding!!.listItem.listView.getChildAt(at.toInt())
            edtQty = childView.findViewById(R.id.edQty)
            edtPrice = childView.findViewById(R.id.edPrice)
            spItem = childView.findViewById(R.id.spItem)

            if (!InputValidation.isEmpty(edtQty) && !InputValidation.isEmpty(edtPrice)) {

                total += (edtQty.text.toString().toInt() * edtPrice.text.toString().toInt())
                val estimateLine = EstimateLine(
                    db.lastID(TablesEntry.ESTIMATE),
                    spItem.selectedItemId,
                    edtQty.text.toString().toInt(),
                    edtPrice.text.toString().toInt(),
                    (edtQty.text.toString().toInt() * edtPrice.text.toString().toInt())
                )

                db.addOneEstimateLine(estimateLine)
            }
        }

        return total
    }



    override fun onDestroy() {
        super.onDestroy()
        configFab(View.VISIBLE)
        _binding = null
    }
}