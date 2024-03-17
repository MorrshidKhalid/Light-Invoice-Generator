package com.example.invoicegenerator.ui.estimate

data class Estimate(val date: String, val clientID: Long, val totalCost: Int, val note: String, val business: String)
