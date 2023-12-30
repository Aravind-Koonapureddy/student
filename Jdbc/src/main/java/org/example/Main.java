package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class Main {
    public static void main(String[] args) throws Exception{
        String jdbcUrl = "jdbc:oracle:thin:@oratestdb2.cwztruwzzvnq.us-east-1.rds.amazonaws.com:1521/TESTDB";
        String username = "tectonic";
        String password = "1nt3grat10n";


            // Load the Oracle JDBC driver
            Class.forName("oracle.jdbc.OracleDriver");

            // Establish a connection to the Oracle database
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            PreparedStatement ps = connection.prepareStatement("insert into SNAP7852T1 values(?,?)");
            ps.setInt(1,2);
            ps.setString(2,"Ravi");
            ps.addBatch();
            int i[] = ps.executeBatch();
        Arrays.stream(i).forEach(x -> System.out.println(x));


    }

}