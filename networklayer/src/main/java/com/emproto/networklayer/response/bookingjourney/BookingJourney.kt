package com.emproto.networklayer.response.bookingjourney

data class BookingJourney(
    val documentation: Documentation,
    val facility: Facility,
    val ownership: Ownership,
    val payments: List<Payment>,
    val possession: Possession,
    val transaction: Transaction,
    val paymentHistory: List<PaymentHistory>,
    val paymentReceipt: ArrayList<PaymentReceipt>

)