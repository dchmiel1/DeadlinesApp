package com.example.deadlinesapp;

import android.provider.BaseColumns;

public class DbNames implements BaseColumns {
    public static final String TABLE_NAME = "tasks";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_SUBJECT = "subject";
    public static final String COLUMN_NAME_IS_DONE = "is_done";
}
