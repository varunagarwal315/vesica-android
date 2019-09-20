package com.example.varun.vesica.database;

/**
 * Created by varun on 11/10/16.
 */
public final class DbConstants {

    //For DbOpenHelper class
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    public static final String CREATE_TABLE= "CREATE TABLE ";

    //For creating tables
    public static final String COLUMN_ID = "id";
    public static final String COMMA =",";
    public static final String INTEGER =" INTEGER";
    public static final String TEXT=" TEXT";
    public static final String PRIMARY_KEY= " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1";


    //For cursor queries
    public static final String SELECT_ALL_FROM ="SELECT * FROM ";
    public static final String SELECT_ONE_FROM ="SELECT id FROM ";
    public static final String EQUALS= " = ";
    public static final String WHERE=" WHERE ";
    public static final String ORDER_BY=" ORDER BY ";




}
