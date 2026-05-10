const mysql = require("mysql2/promise");

const createDatabaseIfNotExists = async () => {

    try {

        // connect WITHOUT database
        const connection = await mysql.createConnection({
            host: process.env.DB_HOST,
            port: process.env.DB_PORT,
            user: process.env.DB_USER,
            password: process.env.DB_PASSWORD
        });

        // create database if not exists
        await connection.query(
            `CREATE DATABASE IF NOT EXISTS ${process.env.DB_NAME}`
        );

        console.log(`Database ${process.env.DB_NAME} ready`);

        await connection.end();

    } catch (error) {

        console.log("Create database error:", error);

    }

};

module.exports = createDatabaseIfNotExists;