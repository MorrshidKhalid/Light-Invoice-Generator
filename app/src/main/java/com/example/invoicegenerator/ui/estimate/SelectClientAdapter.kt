package com.example.invoicegenerator.ui.estimate

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.invoicegenerator.R
import com.example.invoicegenerator.database.InvGContract.TablesEntry
import com.example.invoicegenerator.database.InvGContract.ClientEntry
import com.example.invoicegenerator.database.InvGDB
import com.example.utilityui.Dialog
import com.example.utilityui.Utility

class SelectClientAdapter(
    private val context: Context,
    private val listener: ClientListener
) : RecyclerView.Adapter<SelectClientAdapter.ViewHolder>() {


    // Get dataset of clients from the database.
    private val db = InvGDB(context)
    private var cursor = db.select(TablesEntry.CLIENT)


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val clientPhone: TextView
        val delete: ImageButton
        val select: RadioButton

        init {
            // Define click listener for the ViewHolder's View
            clientPhone = view.findViewById(R.id.client_phone)
            select = view.findViewById(R.id.select_client)
            delete = view.findViewById(R.id.delete)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create views, which defines the UI of the list item.
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_select_client, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        cursor.moveToPosition(position) // Move cursor to current position

        // Load date to views
        holder.select.text = Utility.getColumnDataAsString(cursor, ClientEntry.COLUMN_NAME)
        holder.clientPhone.text = Utility.getColumnDataAsString(cursor, ClientEntry.COLUMN_PHONE)


        // The client Primary-Key.
        val id = Utility.getID(cursor, "_id") // The client Primary-Key.


        // Delete one client
        holder.delete.setOnClickListener{
            // Confirm deletion to the user by showing a dialog to accept delete or reject.
            confirmDeletion(holder.adapterPosition, id)
        }

        // Pass the client id.
        holder.itemView.setOnClickListener {
            listener.onItemClick(id)
        }

        holder.select.setOnClickListener {
            listener.onItemClick(id)
        }
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


    // Handel click listener on item
    interface ClientListener {
        fun onItemClick(id: Long)
    }


    override fun getItemCount(): Int {
        return cursor.count
    }

}