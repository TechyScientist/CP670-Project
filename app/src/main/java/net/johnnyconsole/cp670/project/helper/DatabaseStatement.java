package net.johnnyconsole.cp670.project.helper;

/**
 * @author Johnny Console
 * Registration App DatabaseStatement.java
 * Wrapper for executing SQL statements using
 * parameterized values
 * Last Modified: 26 June 2023
 */
public class DatabaseStatement {

    public String sql;
    public String[] arguments;

    public DatabaseStatement(String sql, String[] arguments) {
        this.sql = sql;
        this.arguments = arguments;
    }

}
