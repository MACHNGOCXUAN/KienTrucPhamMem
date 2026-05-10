const paymentService = require("../services/paymentService");

const createPayment = async (req, res) => {

    try {

        const payment = await paymentService.createPayment(req.body);

        if (payment.status === "FAILED") {

            return res.status(400).json({
                success: false,
                message: "Payment Failed",
                data: payment
            });

        }

        return res.status(201).json({
            success: true,
            message: "Payment Success",
            data: payment
        });

    } catch (error) {

        return res.status(500).json({
            success: false,
            message: error.message
        });

    }

};

module.exports = {
    createPayment
};