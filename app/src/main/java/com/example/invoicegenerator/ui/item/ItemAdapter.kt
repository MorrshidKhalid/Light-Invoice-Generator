package com.example.invoicegenerator.ui.item

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.example.invoicegenerator.R
import com.example.invoicegenerator.database.InvGContract.TablesEntry
import com.example.invoicegenerator.database.InvGContract.ItemEntry
import com.example.invoicegenerator.database.InvGDB
import com.example.utilityui.Dialog
import com.example.utilityui.Utility

class ItemAdapter(
    private val context: Context,
    private val listener: ItemListener,
    order: String
    ) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {


    // Get dataset of clients from the database.
    private val db = InvGDB(context)
    private var cursor = db.select(TablesEntry.ITEM, order)


    /* Reference to the type of views */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val itemName: TextView
        val itemUOM: TextView
        val itemCost: TextView
        val delete: ImageButton

        init {
            // Define click listener for the ViewHolder's View
            itemName = view.findViewById(R.id.itemName)
            itemUOM  = view.findViewById(R.id.itemUOM)
            itemCost = view.findViewById(R.id.itemCost)
            delete     = view.findViewById(R.id.delete)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create views, which defines the UI of the list item.
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_item, parent, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view with data.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        cursor.moveToPosition(position) // Make sure cursor moves to the right position.

        // Load date to views

        holder.itemName.text = Utility.getColumnDataAsString(cursor, ItemEntry.COLUMN_NAME)
        holder.itemCost.text = Utility.getColumnDataAsString(cursor, ItemEntry.COLUMN_BASE_PRICE)
        holder.itemUOM.text = Utility.getColumnDataAsString(cursor, ItemEntry.COLUMN_UOM)

        // The item Primary-Key.
        val id = Utility.getID(cursor, "_id") // The Primary-Key.


        holder.delete.setOnClickListener{
            confirmDeletion(holder.adapterPosition, id) // Display dialog to accept delete or reject.
        }

        // Perform click events on an item in the list.
        holder.itemView.setOnClickListener{
            listener.onClick(id)
        }
    }


    // Return the size of dataset.
    override fun getItemCount(): Int {
        return cursor.count
    }


    private fun deleteItem(id: Long) {
        db.deleteRow(TablesEntry.ITEM, "_id", id)
    }


    // Call this function when ever you want to get the updated dataset.
    private fun newCursor(): Cursor {
        return db.select(TablesEntry.ITEM)
    }


    // Confirm delete by showing dialog to the user
    private fun confirmDeletion(pos: Int, id: Long) {

        Dialog.confirmDelete(context)
            .setNegativeButton(R.string.no) { dialog, _ ->
                // Respond to negative button press
                dialog.cancel()
            }
            .setPositiveButton(R.string.yes) { _, _ ->

                // Respond to positive button press
                deleteItem(id)
                cursor = newCursor() // Refresh the dataset.
                notifyItemRemoved(pos) // Refresh the list.
            }
            .show()
    }



    // Handel click item listener
    interface ItemListener {
        fun onClick(id: Long)
    }

}