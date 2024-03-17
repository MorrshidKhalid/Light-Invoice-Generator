package com.example.invoicegenerator.ui.client

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
import com.example.invoicegenerator.database.InvGContract.ClientEntry
import com.example.invoicegenerator.database.InvGDB
import com.example.utilityui.Dialog
import com.example.utilityui.Utility


class ClientAdapter(
    private val context: Context,
    private val listener: ClientListener
    ) : RecyclerView.Adapter<ClientAdapter.ViewHolder>() {


    // Get set of clients date from the database.
    private val db = InvGDB(context)
    private var cursor = db.select(TablesEntry.CLIENT)

    /* Reference to the type of views */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val clientName: TextView
        val clientPhone: TextView
        val delete: ImageButton

        init {
            // Define click listener for the ViewHolder's View
            clientName = view.findViewById(R.id.clientName)
            clientPhone = view.findViewById(R.id.clientPhone)
            delete = view.findViewById(R.id.delete)
        }

    }


    // Create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create views, which defines the UI of the list item.
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_client, parent, false)

        return ViewHolder(view)
    }


    // Replace the contents of a view with data.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        cursor.moveToPosition(position) // Make sure cursor moves to the right position.

        // Load date to views
        holder.clientName.text = Utility.getColumnDataAsString(cursor, ClientEntry.COLUMN_NAME)
        holder.clientPhone.text = Utility.getColumnDataAsString(cursor, ClientEntry.COLUMN_PHONE)

        val id = Utility.getID(cursor, "_id") // The Primary-Key.


        holder.delete.setOnClickListener{
            confirmDeletion(holder.adapterPosition, id) // Display dialog to accept delete or reject.
        }


        // Perform click events on an item on the list.
        holder.itemView.setOnClickListener{
            listener.onItemClick(id)
        }

    }


    // Handel click listener on item
    interface ClientListener {
        fun onItemClick(id: Long)
    }


    // Return the size of dataset.
    override fun getItemCount(): Int {
        return cursor.count
    }


    private fun deleteClient(id: Long) {

        db.deleteRow(TablesEntry.CLIENT, "_id", id)
    }


    // Call this function when ever you want to get the updated dataset.
    private fun newCursor(): Cursor {
        return db.select(TablesEntry.CLIENT)
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
                deleteClient(id)
                cursor = newCursor() // Refresh the dataset.
                notifyItemRemoved(pos) // Refresh the list.
            }
            .show()
    }
}