package com.kotlinmeetup.repositories

import com.kotlinmeetup.utils.Property
import org.jetbrains.exposed.sql.Database

class Setup {

    init {
        Database.connect(
            url = "${Property["db.host"]}/${Property["db.database"]}",
            driver = Property["db.driver"],
            user = Property["db.user"],
            password = Property["db.pass"]
        )
    }
}