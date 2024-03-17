package com.example.invoicegenerator.ui.estimate_line

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.invoicegenerator.R
import com.example.invoicegenerator.database.InvGContract
import com.example.invoicegenerator.database.InvGContract.EsLineEntry
import com.example.invoicegenerator.database.InvGContract.TablesEntry
import com.example.invoicegenerator.database.InvGDB


class EstimateLineAdapter(
    private val context: Context,
    estimateID: Long)
    : RecyclerView.Adapter<EstimateLineAdapter.ViewHolder>() {


    // Get set of clients date from the database.
    private val db = InvGDB(context)
    private var cursor = db.join(TablesEntry.ESTIMATE_LINE, TablesEntry.ITEM, estimateID)


    /* Reference to the type of views */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvItemName: TextView
        val tvQty: TextView
        val tvPrice: TextView
        val tvTotalCost: TextView


        init {
            // Define click listener for the ViewHolder's View
            tvItemName = view.findViewById(R.id.tvItemName)
            tvQty = view.findViewById(R.id.tvQty)
            tvPrice = view.findViewById(R.id.tvPrice)
            tvTotalCost = view.findViewById(R.id.tvTotalCost)
        }

    }

    // Create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create views, which defines the UI of the list item.
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_details, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        cursor.moveToPosition(position) // Make sure that cursor move to the right position.

        // Load data for this views.
        holder.tvItemName.text = getClientColumnData(InvGContract.ItemEntry.COLUMN_NAME)
        holder.tvQty.text = getClientColumnData(EsLineEntry.COLUMN_QUANTITY)
        holder.tvPrice.text = getClientColumnData(EsLineEntry.COLUMN_PRICE)
        holder.tvTotalCost.text = getClientColumnData(EsLineEntry.COLUMN_TOTAL_LINE_COST)

    }


    // Get client column data
    private fun getClientColumnData(clm: String): String {
        return cursor.getString(cursor.getColumnIndexOrThrow(clm))
    }

    override fun getItemCount(): Int {
        return cursor.count
    }

}