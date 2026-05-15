const { DataTypes } = require("sequelize");

const sequelize = require("../config/database");

const Payment = sequelize.define("Payment", {

    id: {
        type: DataTypes.INTEGER,
        autoIncrement: true,
        primaryKey: true
    },

    bookingId: {
        type: DataTypes.INTEGER,
        allowNull: false
    },

    amount: {
        type: DataTypes.DOUBLE,
        allowNull: false
    },

    paymentMethod: {
        type: DataTypes.ENUM("CASH", "MOMO", "VNPAY"),
        defaultValue: "CASH"
    },

    transactionCode: {
        type: DataTypes.STRING
    },

    status: {   
        type: DataTypes.ENUM("PENDING", "SUCCESS", "FAILED"),
        defaultValue: "PENDING"
    }

}, {
    tableName: "payments"
});

module.exports = Payment;