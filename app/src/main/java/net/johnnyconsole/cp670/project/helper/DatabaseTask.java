package net.johnnyconsole.cp670.project.helper;
import static net.johnnyconsole.cp670.project.helper.ApplicationSession.database;

import android.os.AsyncTask;

/**
 * @author Johnny Console
 * Registration App DtabaseTask.java
 * Uses the AsyncTask interface to asyncronously
 * insert data into the database
 * Last Modified: 26 June 2023
 */
public class DatabaseTask extends AsyncTask<DatabaseStatement, Void, Void> {

    @Override
    protected Void doInBackground(DatabaseStatement... statements) {
        if(statements[0] == null) return null;
        database.execSQL(statements[0].sql, statements[0].arguments);
        return null;
    }
}
