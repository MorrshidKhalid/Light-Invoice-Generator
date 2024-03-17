package com.example.invoicegenerator.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.invoicegenerator.database.InvGContract.*
import com.example.invoicegenerator.ui.client.Client
import com.example.invoicegenerator.ui.estimate.Estimate
import com.example.invoicegenerator.ui.estimate.EstimateLine
import com.example.invoicegenerator.ui.item.Item

class InvGDB(context: Context)
    : SQLiteOpenHelper(context,
    DatabaseEntry.DATABASE_NAME,
    null,
    DatabaseEntry.DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {

        db!!.execSQL(SQL.CREATE_CLIENT_TABLE)
//        db.execSQL(SQL.CREATE_TERMS_TABLE)
        db.execSQL(SQL.CREATE_ITEM_TABLE)
        db.execSQL(SQL.CREATE_BUSINESS_TABLE)
        //db.execSQL(SQL.CREATE_INVOICE_TABLE)
        //db.execSQL(SQL.CREATE_INVOICE_LINE_TABLE)
        //db.execSQL(SQL.CREATE_PAYMENT_TABLE)
        db.execSQL(SQL.CREATE_ESTIMATE_TABLE)
        db.execSQL(SQL.CREATE_ESTIMATE_LINE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }


    // Open database in write mode.
    private fun openDatabaseWrite(): SQLiteDatabase {
        return this.writableDatabase
    }

    // Open database in read mode.
    private fun openDatabaseRead(): SQLiteDatabase {
        return this.readableDatabase
    }


    /**
     *
     * Select all data from specific table.
     * @param tableName the table you want retrieve data from.
     * @return  the dataset.
     */
    fun select(tableName: String, order: String = "DESC"): Cursor {

        // Open database in read mode and,
        // access database queries
        return openDatabaseRead().rawQuery(
            "SELECT * FROM $tableName ORDER BY _id $order",
            null
        )
    }


    /**
     *
     * Select all data from specific table with condition.
     * @param tableName the table you want retrieve data from.
     * @param whereClause the column you specified.
     * @param id the Primary-Key.
     */
    fun select(tableName: String, whereClause: String, id: Long): Cursor {

        // Open database in read mode and,
        // access database queries
        return openDatabaseRead().rawQuery(
            "SELECT * FROM $tableName WHERE $whereClause = $id ORDER BY  _id DESC",
            null
        )
    }


    /**
     * To delete a row from the database
     * @param tableName the table you want to delete from.
     * @param whereClause the column name.
     * @param rowValue the column value.
     */
    fun deleteRow(tableName: String, whereClause: String, rowValue: Long) {

        // Open database in write mode.
        // access database queries
        openDatabaseWrite().delete(tableName, "$whereClause= ?", arrayOf(rowValue.toString()))
    }



    fun join(mainTable: String, secTable: String, id: Long): Cursor {

        // Open database in read mode, and access database queries
        return openDatabaseRead().rawQuery(
            "SELECT Name, Quantity, Price, Total_line_cost FROM $mainTable"
                    + " LEFT JOIN $secTable ON $mainTable.L_item_id = $secTable._id"
                    + " WHERE $mainTable.Es_id " + "=" + id, null
        )
    }

    /**
     *
     * Add one client to database.
     * @param client data coming from the user.
     * @return if data inserted successfully.
     */
    fun addOneClient(client: Client): Boolean {


        // Create a new values coming from the user
        val values = ContentValues().apply {
            put(ClientEntry.COLUMN_NAME, client.name)
            put(ClientEntry.COLUMN_PHONE, client.phone)
            put(ClientEntry.COLUMN_EMAIL, client.email)
        }

        // Open database in write mode and, access database queries.
        // Insert new row and return if save successfully done.
        return openDatabaseWrite().insert(TablesEntry.CLIENT, null, values).toInt() != -1
    }

    /**
     * Update client info
     */
    fun updateClient(client: Client, id: Long): Boolean {
        // ContentValues object to hold the new values
        val values = ContentValues().apply {

            put(ClientEntry.COLUMN_NAME, client.name)
            put(ClientEntry.COLUMN_PHONE, client.phone)
            put(ClientEntry.COLUMN_EMAIL, client.email)
        }

        // Open database in write mode and, access database queries.
        return openDatabaseWrite().update(
            TablesEntry.CLIENT,
            values,
            "_id = ?",
            arrayOf(id.toString())
        ) != -1
    }


    /**
     *
     * Add one item to database.
     * @param item data coming from the user.
     * @return if data inserted successfully.
     */
    fun addOneItem(item: Item): Boolean {

        // Create a new values coming from the user
        val values = ContentValues().apply {
            put(ItemEntry.COLUMN_NAME, item.name)
            put(ItemEntry.COLUMN_UOM, item.uom)
            put(ItemEntry.COLUMN_DESCRIPTION, item.description)
            put(ItemEntry.COLUMN_CURRENCY, item.currency)
            put(ItemEntry.COLUMN_BASE_PRICE, item.baseCost)

        }

        // Open database in write mode and, access database queries and,
        // insert new row and return if save successfully done
        return openDatabaseWrite().insert(TablesEntry.ITEM, null, values).toInt() != -1
    }



    fun updateItem(item: Item, id: Long): Boolean {

        // ContentValues object to hold the new values
        val values = ContentValues().apply {
            put(ItemEntry.COLUMN_NAME, item.name)
            put(ItemEntry.COLUMN_UOM, item.uom)
            put(ItemEntry.COLUMN_DESCRIPTION, item.description)
            put(ItemEntry.COLUMN_CURRENCY, item.currency)
            put(ItemEntry.COLUMN_BASE_PRICE, item.baseCost)
        }

        // Open database in write mode and, access database queries.
        return openDatabaseWrite().update(
            TablesEntry.ITEM,
            values,
            "_id = ?",
            arrayOf(id.toString())
        ) != -1
    }


    fun addOneEstimate(estimate: Estimate): Boolean {

        // Create a new values coming from the user
        val values = ContentValues().apply {
            put(EstimateEntry.COLUMN_ISSUED_DATE, estimate.date)
            put(EstimateEntry.COLUMN_ES_CLIENT_ID, estimate.clientID)
            put(EstimateEntry.COLUMN_TOTAL, estimate.totalCost)
            put(EstimateEntry.COLUMN_NOTE, estimate.note)
            put(EstimateEntry.COLUMN_BUSINESS, estimate.business)
        }

        // Open database in write mode and, access database queries and,
        // insert new row and return if save successfully done
        return openDatabaseWrite().insert(TablesEntry.ESTIMATE, null, values).toInt() != -1
    }


    fun addOneEstimateLine(estimateLine: EstimateLine): Boolean {

        // Create a new values coming from the user
        val values = ContentValues().apply {
            put(EsLineEntry.COLUMN_ES_ID, estimateLine.estimateID)
            put(EsLineEntry.COLUMN_L_ITEM_ID, estimateLine.lineItemID)
            put(EsLineEntry.COLUMN_PRICE, estimateLine.price)
            put(EsLineEntry.COLUMN_QUANTITY, estimateLine.qty)
            put(EsLineEntry.COLUMN_TOTAL_LINE_COST, estimateLine.lineCost)
        }

        // Open database in write mode and, access database queries and,
        // insert new row and return if save successfully done
        return openDatabaseWrite().insert(TablesEntry.ESTIMATE_LINE, null, values).toInt() != -1
    }


    /**
     *  Get the last Primary-Key of a specific table
     * @param tableName The table you want to get the it's last Primary-Key.
     * @return the last Primary-Key.
     */
    fun lastID(tableName: String): Long {

        val query = "SELECT MAX(_id) FROM $tableName"
        val cursor = writableDatabase.rawQuery(query, null)
        var lastId = -1

        if (cursor.moveToFirst()) {
            lastId = cursor.getInt(0)
        }
        cursor.close()
        return lastId.toLong()
    }


    fun updateRow(tableName: String, clmName:String, newValue: Int, id: Long) {

        // Create a new values.
        val values = ContentValues().apply {
            put(clmName, newValue)
        }

        val whereClause: String = "_id" + "=?"
        val whereArgs = arrayOf(id.toString())
        openDatabaseWrite().update(tableName, values, whereClause, whereArgs)
    }
}