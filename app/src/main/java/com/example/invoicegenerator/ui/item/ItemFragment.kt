package com.example.invoicegenerator.ui.item

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
import com.example.invoicegenerator.database.InvGContract.Order
import com.example.invoicegenerator.databinding.FragmentItemBinding
import com.example.utilityui.DynamicList
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ItemFragment : Fragment() {

    // Direct reference to the components on the layout that have an ID.
    private var _binding: FragmentItemBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Bring the main button to the front of everything.
        accessMainButton().bringToFront()

        // Inflate the layout for this fragment.
        _binding = FragmentItemBinding.inflate(inflater, container, false)

        // Return the root for this fragment.
        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        handelToolBar()
        displayItemOnList(Order.ORDER_BY_NEWEST) // Display items in newest order.

        // Add new item
        accessMainButton().setOnClickListener {
            navigateToNewItem()
        }
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
                menuInflater.inflate(R.menu.menu_search_sort, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {

                    R.id.menu_search -> {


                        true
                    }

                    R.id.menu_sort_by_newest -> {

                        displayItemOnList(Order.ORDER_BY_NEWEST) // Display items in newest order.
                        true
                    }

                    R.id.menu_sort_by_oldest -> {

                        displayItemOnList(Order.ORDER_BY_OLDEST) // Display items in oldest order.
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


    // Navigate to (New item fragment).
    private fun navigateToNewItem() {
        findNavController().navigate(R.id.action_item_to_new)
    }



    private fun navigateToUpdateItem(id: Long) {

        val action = ItemFragmentDirections.actionItemToUpdate(id)
        findNavController().navigate(action)
    }


    // Set the custom adapter
    private fun itemAdapter(order: String): ItemAdapter {
        return ItemAdapter(requireContext(), object : ItemAdapter.ItemListener {
            override fun onClick(id: Long) {
                navigateToUpdateItem(id)
            }
        }, order)
    }


    // Display item in dynamic for this layout.
    private fun displayItemOnList(order: String) {

        DynamicList.setupVertically(_binding!!.rvItem, requireContext()) // Prepare RecyclerView.
        _binding!!.rvItem.adapter = itemAdapter(order)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}