package com.example.invoicegenerator.database

import android.provider.BaseColumns

class InvGContract {


    /* anonymous object defines the (Database name and version) */
    object DatabaseEntry {

        const val DATABASE_NAME :String = "InvGDB.db"
        const val DATABASE_VERSION :Int = 1
    }


    /* anonymous object defines the (Table names) */
    object TablesEntry {

        //  Entity tables.
        const val ITEM :String = "ITEM"
        const val INVOICE :String = "INVOICE"
        const val CLIENT :String = "CLIENT"
        const val TERMS = "TERMS"
        const val ESTIMATE :String = "ESTIMATE"
        const val BUSINESS :String = "BUSINESS"


        // Relational tables.
        const val PAYMENT :String = "PAYMENT"
        const val INVOICE_LINE :String = "INVOICE_LINE"
        const val ESTIMATE_LINE :String = "ESTIMATE_LINE"
    }


    /* anonymous object defines the (INVOICE table contents) */
    object InvEntry : BaseColumns {

        const val COLUMN_ISSUED_DATE = "Issued_date"
        const val COLUMN_DUE_DATE = "Due_date"
        const val COLUMN_INV_CLIENT_ID = "Inv_client_id"
        const val COLUMN_TYPE = "Type"
        const val COLUMN_TOTAL = "Total_amount"
        const val COLUMN_STATUS = "Status"
        const val COLUMN_INV_TERM_ID = "Inv_term_id"
        const val COLUMN_NOTE = "Note"
        const val COLUMN_DISCOUNT = "Discount"

    }


    /* anonymous object defines the (TERM table contents) */
    object TermsEntry : BaseColumns {

        const val COLUMN_TERM_DES = "TDes"
    }



    /* anonymous object defines the (BUSINESS table contents) */
    object BusinessEntry : BaseColumns {

        const val COLUMN_BUSINESS_NAME = "Name"
    }


    /* anonymous object defines the (CLIENT table contents) */
    object ClientEntry : BaseColumns {

        const val COLUMN_NAME = "Name"
        const val COLUMN_EMAIL = "Email"
        const val COLUMN_PHONE = "Phone"
    }


    /* anonymous object defines the (ITEM table contents) */
    object ItemEntry : BaseColumns {

        const val COLUMN_ITEM_TERM = "I_term_id"
        const val COLUMN_NAME = "Name"
        const val COLUMN_UOM = "Uom"
        const val COLUMN_DESCRIPTION = "Description"
        const val COLUMN_CURRENCY = "Currency"
        const val COLUMN_BASE_PRICE = "Base_price"
    }

    /* anonymous object defines the (ESTIMATE table contents) */
    object EstimateEntry : BaseColumns {

        const val COLUMN_ISSUED_DATE = "Issued_date"
        const val COLUMN_ES_CLIENT_ID = "Es_client_id"
        const val COLUMN_TYPE = "Type"
        const val COLUMN_TOTAL = "Total_amount"
        const val COLUMN_ES_TERM_ID = "Es_term_id"
        const val COLUMN_NOTE = "Note"
        const val COLUMN_BUSINESS = "Business"
    }


    /* anonymous object defines the (PAYMENT table contents) */
    object PaymentEntry : BaseColumns {

        const val COLUMN_INV_ID = "Inv_id"
        const val COLUMN_DATE = "Date"
        const val COLUMN_AMOUNT = "Amount"
    }


    /* anonymous object defines the (INVOICE_LINE table contents) */
    object InvLineEntry : BaseColumns {

        const val COLUMN_INV_ID = "Inv_id"
        const val COLUMN_L_ITEM_ID = "L_item_id"
        const val COLUMN_QUANTITY = "Quantity"
        const val COLUMN_UNITE_PRICE = "Unite_price"
    }



    /* anonymous object defines the (ESTIMATE_LINE table contents) */
    object EsLineEntry : BaseColumns {

        const val COLUMN_ES_ID = "Es_id"
        const val COLUMN_L_ITEM_ID = "L_item_id"
        const val COLUMN_QUANTITY = "Quantity"
        const val COLUMN_PRICE = "Price"
        const val COLUMN_TOTAL_LINE_COST = "Total_line_cost"
    }


    /* anonymous object defines the (ESTIMATE_LINE table contents) */
    object Order {

        const val ORDER_BY_NEWEST = "DESC"
        const val ORDER_BY_OLDEST = "ASC"
    }
}