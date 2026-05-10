const Payment = require("../models/Payment");

const createPayment = async (data) => {

    const isSuccess = Math.random() > 0.5;

    const payment = await Payment.create({

        bookingId: data.bookingId,

        amount: data.amount,

        paymentMethod: data.paymentMethod,

        transactionCode: "TXN_" + Date.now(),

        status: isSuccess ? "SUCCESS" : "FAILED"
    });

    return payment;
};

module.exports = {
    createPayment
};