package com.example.invoicegenerator.database

import android.provider.BaseColumns
import com.example.invoicegenerator.database.InvGContract.*

object SQL : BaseSql {

        const val CREATE_TERMS_TABLE =
            "CREATE TABLE ${TablesEntry.TERMS} (" +
                    "${BaseColumns._ID} ${BaseSql.PK}" +
                    "${TermsEntry.COLUMN_TERM_DES} ${BaseSql.TXT})"


    const val CREATE_BUSINESS_TABLE =
        "CREATE TABLE ${TablesEntry.BUSINESS} (" +
                "${BaseColumns._ID} ${BaseSql.PK}" +
                "${BusinessEntry.COLUMN_BUSINESS_NAME} ${BaseSql.TXT})"


        const val CREATE_CLIENT_TABLE =
            "CREATE TABLE ${TablesEntry.CLIENT} (" +
                    "${BaseColumns._ID} ${BaseSql.PK}" +
                    "${ClientEntry.COLUMN_NAME} ${BaseSql.TXT}," +
                    "${ClientEntry.COLUMN_PHONE} ${BaseSql.TXT}," +
                    "${ClientEntry.COLUMN_EMAIL} ${BaseSql.TXT})"


        const val CREATE_ITEM_TABLE =
            "CREATE TABLE ${TablesEntry.ITEM} (" +
                    "${BaseColumns._ID} ${BaseSql.PK}" +
                    "${ItemEntry.COLUMN_NAME} ${BaseSql.TXT}," +
                    "${ItemEntry.COLUMN_UOM} ${BaseSql.TXT}," +
                    "${ItemEntry.COLUMN_DESCRIPTION} ${BaseSql.TXT}," +
                    "${ItemEntry.COLUMN_CURRENCY} ${BaseSql.TXT}," +
                    "${ItemEntry.COLUMN_BASE_PRICE} ${BaseSql.REAL})"
//                    "${ItemEntry.COLUMN_ITEM_TERM} ${BaseSql.INT})"
//                    "FOREING KEY(${ItemEntry.COLUMN_ITEM_TERM}) REFERENCE ${TablesEntry.TERMS}(${BaseColumns._ID}"



        const val CREATE_INVOICE_TABLE =
            "CREATE TABLE ${TablesEntry.INVOICE} (" +
                    "${BaseColumns._ID} ${BaseSql.PK}" +
                    "${InvEntry.COLUMN_ISSUED_DATE} ${BaseSql.TXT}," +
                    "${InvEntry.COLUMN_DUE_DATE} ${BaseSql.TXT}," +
                    "${InvEntry.COLUMN_INV_CLIENT_ID} ${BaseSql.INT}," +
                    "${InvEntry.COLUMN_TYPE} ${BaseSql.TXT}," +
                    "${InvEntry.COLUMN_TOTAL} ${BaseSql.REAL}," +
                    "${InvEntry.COLUMN_STATUS} ${BaseSql.TXT}," +
                    "${InvEntry.COLUMN_INV_TERM_ID} ${BaseSql.INT}," +
                    "${InvEntry.COLUMN_NOTE} ${BaseSql.TXT}," +
                    "${InvEntry.COLUMN_DISCOUNT} ${BaseSql.REAL})"
//                    "FOREING KEY(${InvEntry.COLUMN_INV_CLIENT_ID}) REFERENCE ${TablesEntry.CLIENT} (${BaseColumns._ID})," +
//                    "FOREING KEY(${InvEntry.COLUMN_INV_TERM_ID}) REFERENCE ${TablesEntry.TERMS} (${BaseColumns._ID});)"



        const val CREATE_PAYMENT_TABLE =
            "CREATE TABLE ${TablesEntry.PAYMENT} (" +
                    "${BaseColumns._ID} ${BaseSql.PK}" +
                    "${PaymentEntry.COLUMN_INV_ID} ${BaseSql.INT}" +
                    "${PaymentEntry.COLUMN_DATE} ${BaseSql.TXT}" +
                    "${PaymentEntry.COLUMN_AMOUNT} ${BaseSql.REAL}" +
                    "FOREING KEY(${PaymentEntry.COLUMN_INV_ID}) REFERENCE ${TablesEntry.INVOICE} (${BaseColumns._ID});)"


        const val CREATE_INVOICE_LINE_TABLE =
            "CREATE TABLE ${TablesEntry.INVOICE_LINE} (" +
                    "${BaseColumns._ID} ${BaseSql.PK}" +
                    "${InvLineEntry.COLUMN_INV_ID} ${BaseSql.INT}" +
                    "${InvLineEntry.COLUMN_L_ITEM_ID} ${BaseSql.INT}" +
                    "${InvLineEntry.COLUMN_QUANTITY} ${BaseSql.INT}" +
                    "${InvLineEntry.COLUMN_UNITE_PRICE} ${BaseSql.REAL}" +
                    "FOREING KEY(${InvLineEntry.COLUMN_INV_ID}) REFERENCE ${TablesEntry.INVOICE} (${BaseColumns._ID})," +
                    "FOREING KEY(${InvLineEntry.COLUMN_L_ITEM_ID}) REFERENCE ${TablesEntry.ITEM} (${BaseColumns._ID});)"


        const val CREATE_ESTIMATE_TABLE =
            "CREATE TABLE ${TablesEntry.ESTIMATE} (" +
                    "${BaseColumns._ID} ${BaseSql.PK}"    +
                    "${EstimateEntry.COLUMN_ISSUED_DATE} ${BaseSql.TXT}," +
                    "${EstimateEntry.COLUMN_BUSINESS} ${BaseSql.TXT}," +
                    "${EstimateEntry.COLUMN_ES_CLIENT_ID} ${BaseSql.INT}," +
                    "${EstimateEntry.COLUMN_TOTAL} ${BaseSql.REAL}," +
                    "${EstimateEntry.COLUMN_NOTE} ${BaseSql.TXT})"



        const val CREATE_ESTIMATE_LINE_TABLE =
            "CREATE TABLE ${TablesEntry.ESTIMATE_LINE} (" +
                    "${BaseColumns._ID} ${BaseSql.PK}" +
                    "${EsLineEntry.COLUMN_ES_ID} ${BaseSql.INT}," +
                    "${EsLineEntry.COLUMN_L_ITEM_ID} ${BaseSql.INT}," +
                    "${EsLineEntry.COLUMN_QUANTITY} ${BaseSql.INT}," +
                    "${EsLineEntry.COLUMN_PRICE} ${BaseSql.REAL}," +
                    "${EsLineEntry.COLUMN_TOTAL_LINE_COST} ${BaseSql.REAL})"

}