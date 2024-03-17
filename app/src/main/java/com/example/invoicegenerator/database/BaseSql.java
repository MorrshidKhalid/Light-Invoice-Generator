package com.example.invoicegenerator.database;

public interface BaseSql {

    // The sql query that represent the primary key of a row.
    String PK = "INTEGER PRIMARY KEY AUTOINCREMENT,";

    // The sql query that represent TEXT datatype.
    String TXT = "TEXT NOT NULL";

    // The sql query that represent Decimal datatype with default value of 0.
    String REAL = "REAL NOT NULL DEFAULT 0";

    // The sql query that represent Integer datatype.
    String INT = "INTEGER";
}
