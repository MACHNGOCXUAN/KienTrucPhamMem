require("dotenv").config();

const app = require("./app");

const sequelize = require("./config/database");

const createDatabaseIfNotExists = require("./config/initDatabase");

const PORT = process.env.PORT || 8084;

async function startServer() {

    try {

        // create db if not exists
        await createDatabaseIfNotExists();

        // connect sequelize
        await sequelize.authenticate();

        console.log("MariaDB Connected");

        // create tables
        await sequelize.sync();

        console.log("Database Synced");

        app.listen(PORT, () => {

            console.log(`Payment Service running on port ${PORT}`);

        });

    } catch (error) {

        console.log(error);

    }

}

startServer();