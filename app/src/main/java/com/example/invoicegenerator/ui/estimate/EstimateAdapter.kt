package com.example.invoicegenerator.ui.estimate

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.invoicegenerator.R
import com.example.invoicegenerator.database.InvGContract.EstimateEntry
import com.example.invoicegenerator.database.InvGContract.TablesEntry
import com.example.invoicegenerator.database.InvGDB
import com.example.utilityui.Dialog
import com.example.utilityui.Utility


class EstimateAdapter(
    private val context: Context,
    private val listener: EstimateListener
    ) : RecyclerView.Adapter<EstimateAdapter.ViewHolder>(){


    // Get set of estimates date from the database.
    private val db = InvGDB(context)
    private var cursor = db.select(TablesEntry.ESTIMATE)

    /* Reference to the type of views */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val issuingDate: TextView
        val totalCost: TextView
        val business: TextView
        val delete: ImageButton

        init {
            // Define click listener for the ViewHolder's View
            issuingDate = view.findViewById(R.id.issuingDate)
            business = view.findViewById(R.id.business)
            totalCost = view.findViewById(R.id.totalCost)
            delete = view.findViewById(R.id.delete)
        }

    }


    // Create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create views, which defines the UI of the list item.
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_estimate, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        cursor.moveToPosition(position) // Make sure you move the cursor right position.

        // Load date to views
        holder.issuingDate.text = Utility.getColumnDataAsString(cursor, EstimateEntry.COLUMN_ISSUED_DATE)
        holder.business.text = Utility.getColumnDataAsString(cursor, EstimateEntry.COLUMN_BUSINESS)
        val total = "TOTAL: ${Utility.getColumnDataAsString(cursor, EstimateEntry.COLUMN_TOTAL)}" // used to concatenate.
        holder.totalCost.text = total

        val id = Utility.getID(cursor, "_id") // The Primary-Key.

        holder.delete.setOnClickListener {
            // Confirm deletion to the user by showing a dialog to accept delete or reject.
            confirmDeletion(holder.adapterPosition, id)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(id)
        }
    }


    // Handel click listener on item
    interface EstimateListener {
        fun onItemClick(id: Long)
    }


    private fun deleteEstimate(id: Long) {
        db.deleteRow(TablesEntry.ESTIMATE, "_id", id)
    }


    // Call this function when ever you want to get the updated dataset.
    private fun newCursor(): Cursor {
        return db.select(TablesEntry.ESTIMATE)
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
                deleteEstimate(id)
                cursor = newCursor() // Refresh the dataset.
                notifyItemRemoved(pos) // Refresh the list.
            }
            .show()
    }


    override fun getItemCount(): Int {
        return cursor.count
    }
}