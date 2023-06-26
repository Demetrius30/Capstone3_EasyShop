package org.yearup.data.mysql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class MySqlDaoBase
{
    public DataSource dataSource;

    public MySqlDaoBase(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }//I made this public because I was getting RED (don't know if its suppose to be private or public)

    protected Connection getConnection() throws SQLException
    {
        return dataSource.getConnection();
    }
}
