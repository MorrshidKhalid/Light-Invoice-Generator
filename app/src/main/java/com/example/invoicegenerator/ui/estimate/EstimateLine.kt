package com.example.invoicegenerator.ui.estimate

data class EstimateLine(val estimateID: Long, val lineItemID: Long, val qty: Int, val price: Int, val lineCost: Int)
