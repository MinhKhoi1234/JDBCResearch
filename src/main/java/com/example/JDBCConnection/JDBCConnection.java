package com.example.JDBCConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.example.model.Input;
import com.example.model.SubTaskA;
import com.example.model.SubTaskB;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

public class JDBCConnection {
    public JDBCConnection() {
    }

    private static final String DATABASE1 = "jdbc:sqlite:database/TempPopulation.db";
    private static final String DATABASE3 = "jdbc:sqlite:database/climatechange.db";
    // private static final String DATABASE2 = "jdbc:sqlite:database/Personas + Students.db";

    public ArrayList<String> getResultByCountryName() { //change the return type (TODO) (this method gets an ArrayList of country names)
        ArrayList<String> Countries = new ArrayList<String>(); //change the return type(TODO)

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE1); //Change database (TODO)

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT DISTINCT Country FROM GlobalYearlyLandTempByCity;"; // change the query(TODO)
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String countryName     = results.getString("Country"); //Change the "Country" to the desired column (TODO)

                // Store the movieName in the ArrayList to return
                Countries.add(countryName);
            } // If the result isn't an array, then reference getStartYear() method below (TODO)

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return Countries; //change return (TODO)
    }

    public int getStartEndGlobalYearlyLandTempYear(String MINorMAX) { //the available years for yearly temperature on land globally
        int year = JDBCConnection.getYear(MINorMAX, "LAND");
        return year;
    }

    public int getStartEndGlobalYearlyTempYear(String MINorMAX) { //the available years for yearly temperature globally
        int year = JDBCConnection.getYear(MINorMAX, "GLOBAL");
        return year;
    }

    public int getStartEndYearPopulation(String MINorMAX) { //the available years for population globally
        int year = JDBCConnection.getYear(MINorMAX, "POPULATION");;

        return year;
    }

    private static int getYear(String MINorMAX, String LANDorGLOBALorPopulation) { //the above 3 methods get the year from this method
        int year = 0;
        
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);//

            Statement statement = connection.createStatement();//
            statement.setQueryTimeout(30);

            String query = "";

            if(LANDorGLOBALorPopulation.equalsIgnoreCase("LAND")) {
                query = "SELECT " + MINorMAX + "(Year) FROM GlobalYearlyLandTempByCountry;";
            } else if (LANDorGLOBALorPopulation.equalsIgnoreCase("GLOBAL")) {
                query = "SELECT " + MINorMAX + "(Year) FROM GlobalYearlyTemp;";
            } else if (LANDorGLOBALorPopulation.equalsIgnoreCase("Population")) {
                query = "SELECT " + MINorMAX + "(Year) FROM Population;";
            }

            ResultSet results = statement.executeQuery(query);
            year     = results.getInt(1);

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return year;
    }

    public ArrayList<Input> getCityAndTemp(String countryName, String CityORState, long startYear, long endYear) { 
        //this gets cities/states, avgTemp,minTemp, maxTemp from the 4 methods below and store in an ArrayList of type Input
        int i;
        ArrayList<Input> inputs = new ArrayList<>();
        ArrayList<String> inputs1 = getCitiesStateByCountryName(countryName, CityORState);
        ArrayList<Double> inputs2 = getStartEndCityStateAvgTempChange(countryName, CityORState, startYear, endYear);
        ArrayList<Double> inputs3 = getStartEndCityStateMinTempChange(countryName, CityORState, startYear, endYear);
        ArrayList<Double> inputs4 = getStartEndCityStateMaxTempChange(countryName, CityORState, startYear, endYear);

        for(i = 0; i < inputs1.size(); ++i) {
            Input input = new Input();
            input.setCity(inputs1.get(i));
            input.setAvgTemp(inputs2.get(i));
            input.setMinTemp(inputs3.get(i));
            input.setMaxTemp(inputs4.get(i));
            inputs.add(input);
        }

        return inputs;
    }
    
    public static ArrayList<Double> getStartEndCityStateAvgTempChange(String countryName, String CityORState, long startYear, long endYear) { //avgTemp change by proportion
        ArrayList<Double> TempStartYear = new ArrayList<Double>();
        ArrayList<Double> TempEndYear = new ArrayList<Double>();
        ArrayList<Double> AvgTempChange = new ArrayList<Double>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM GlobalYearlyLandTempBy";
            String query1 = "SELECT * FROM GlobalYearlyLandTempBy";

            if(CityORState.equalsIgnoreCase("City")) {
                query = query + "City WHERE Country = '" + countryName + "' AND Year = '" + startYear + "';";
            } else if(CityORState.equalsIgnoreCase("State")) {
                query = query + "State WHERE Country = '" + countryName + "' AND Year = '" + startYear + "';";
            }

            if(CityORState.equalsIgnoreCase("City")) {
                query1 = query1 + "City WHERE Country = '" + countryName + "' AND Year = '" + endYear + "';";
            } else if(CityORState.equalsIgnoreCase("State")) {
                query1 = query1 + "State WHERE Country = '" + countryName + "' AND Year = '" + endYear + "';";
            }

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {

                double temp = 0;

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getDouble("AverageTemperature");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getDouble("AverageTemperature");
                }


                TempStartYear.add(temp);
            }

            results = statement.executeQuery(query1);

            while (results.next()) {

                double temp = 0;

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getDouble("AverageTemperature");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getDouble("AverageTemperature");
                }


                TempEndYear.add(temp);
            }

            for(int i = 0; i < TempStartYear.size(); ++i) {
                double change = ((TempEndYear.get(i) - TempStartYear.get(i)) / TempStartYear.get(i)) * 100;
                AvgTempChange.add(Math.abs(change));
            }


            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return AvgTempChange;
    }

    public static ArrayList<Double> getStartEndCityStateMinTempChange(String countryName, String CityORState, long startYear, long endYear) { //minTemp change by proportion
        ArrayList<Double> TempStartYear = new ArrayList<Double>();
        ArrayList<Double> TempEndYear = new ArrayList<Double>();
        ArrayList<Double> MinTempChange = new ArrayList<Double>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM GlobalYearlyLandTempBy";
            String query1 = "SELECT * FROM GlobalYearlyLandTempBy";

            if(CityORState.equalsIgnoreCase("City")) {
                query = query + "City WHERE Country = '" + countryName + "' AND Year = '" + startYear + "';";
            } else if(CityORState.equalsIgnoreCase("State")) {
                query = query + "State WHERE Country = '" + countryName + "' AND Year = '" + startYear + "';";
            }

            if(CityORState.equalsIgnoreCase("City")) {
                query1 = query1 + "City WHERE Country = '" + countryName + "' AND Year = '" + endYear + "';";
            } else if(CityORState.equalsIgnoreCase("State")) {
                query1 = query1 + "State WHERE Country = '" + countryName + "' AND Year = '" + endYear + "';";
            }

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {

                double temp = 0;

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getDouble("MinimumTemperature");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getDouble("MinimumTemperature");
                }

                TempStartYear.add(temp);
            }

            results = statement.executeQuery(query1);

            while (results.next()) {

                double temp = 0;

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getDouble("MinimumTemperature");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getDouble("MinimumTemperature");
                }

                TempEndYear.add(temp);
            }

            for(int i = 0; i < TempStartYear.size(); ++i) {
                double change = ((TempEndYear.get(i) - TempStartYear.get(i)) / TempStartYear.get(i)) * 100;
                MinTempChange.add(Math.abs(change));
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return MinTempChange;
    }

    public static ArrayList<Double> getStartEndCityStateMaxTempChange(String countryName, String CityORState, long startYear, long endYear) { //maxTemp change by proportion
        ArrayList<Double> TempStartYear = new ArrayList<Double>();
        ArrayList<Double> TempEndYear = new ArrayList<Double>();
        ArrayList<Double> MaxTempChange = new ArrayList<Double>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM GlobalYearlyLandTempBy";
            String query1 = "SELECT * FROM GlobalYearlyLandTempBy";

            if(CityORState.equalsIgnoreCase("City")) {
                query = query + "City WHERE Country = '" + countryName + "' AND Year = '" + startYear + "';";
            } else if(CityORState.equalsIgnoreCase("State")) {
                query = query + "State WHERE Country = '" + countryName + "' AND Year = '" + startYear + "';";
            }

            if(CityORState.equalsIgnoreCase("City")) {
                query1 = query1 + "City WHERE Country = '" + countryName + "' AND Year = '" + endYear + "';";
            } else if(CityORState.equalsIgnoreCase("State")) {
                query1 = query1 + "State WHERE Country = '" + countryName + "' AND Year = '" + endYear + "';";
            }

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {

                double temp = 0;

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getDouble("MaximumTemperature");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getDouble("MaximumTemperature");
                }

                TempStartYear.add(temp);
            }

            results = statement.executeQuery(query1);

            while (results.next()) {

                double temp = 0;

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getDouble("MaximumTemperature");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getDouble("MaximumTemperature");
                }

                TempEndYear.add(temp);
            }

            for(int i = 0; i < TempStartYear.size(); ++i) {
                double change = ((TempEndYear.get(i) - TempStartYear.get(i)) / TempStartYear.get(i)) * 100;
                MaxTempChange.add(Math.abs(change));
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return MaxTempChange;
    }

    public static ArrayList<String> getCitiesStateByCountryName(String countryName, String CityORState) { //cities/state of the country user typed
        ArrayList<String> CitiesState = new ArrayList<String>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM GlobalYearlyLandTempBy";

            if(CityORState.equalsIgnoreCase("City")) {
                query = query + "City WHERE Country = '" + countryName + "' AND Year = '1950';";
            } else if (CityORState.equalsIgnoreCase("State")) {
                query = query + "State WHERE Country = '" + countryName + "' AND Year = '1950';";
            }
            
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {

                String citiesState = "";

                if(CityORState.equalsIgnoreCase("City")) {
                    citiesState     = results.getString("City");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    citiesState     = results.getString("State");
                }

                CitiesState.add(citiesState);
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return CitiesState;
    }

    //Add more here

    // subTask B

    public ArrayList<SubTaskB> subTaskBTask1(int startingYear, int timePeriod, String selectedRegion, int region, int option, int mode, boolean mostSimilar) {
        // region 1  = country, 2 = state, 3 = city
        // option 1 = temperature, 2 = population, 3 = both
        // mode 1 = absolute, 2 = relative
        ArrayList<SubTaskB> result = new ArrayList<SubTaskB>();
        switch(option){
            case 1:
                result = similarTemp(startingYear, timePeriod, selectedRegion, region, mode);
                break;
            case 2:
                if (region == 1){
                    result = similarPopulation(startingYear, timePeriod, selectedRegion, mode);
                    break;
                }
                else{
                    break;
                }
            case 3:
                if (region == 1){
                    result = similarTempAndPopulation(startingYear, timePeriod, selectedRegion, mode);
                    break;
                }
                else{
                    break;
                }
        }

        if(mostSimilar){
            SortLowToHigh(result);
        }
        else{
            SortHighToLow(result);
        }

        return result;
    }

    public static ArrayList<SubTaskB> similarTemp (int startingYear, int timePeriod, String selectedRegion, int region, int mode){
        ArrayList<SubTaskB> result = new ArrayList<>();
        
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE3);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the country exists
            String query = "SELECT EXISTS";

            switch(region){
                case 1:
                    query = query + " (SELECT 1 FROM GlobalYearlyLandTempByCountry WHERE Country = '" + selectedRegion + "' AND (Year BETWEEN'" + startingYear + "' AND '" + (startingYear + timePeriod - 1) + "')";
                    break;
                case 2:
                    query = query + " (SELECT 1 FROM GlobalYearlyLandTempByState WHERE State = '" + selectedRegion + "' AND (Year BETWEEN'" + startingYear + "' AND '" + (startingYear + timePeriod - 1) + "'))";
                    break;
                case 3:
                    query = query + " (SELECT 1 FROM GlobalYearlyLandTempByCity WHERE City = '" + selectedRegion + "'  AND (Year BETWEEN'" + startingYear + "' AND '" + (startingYear + timePeriod - 1) + "'))";
                    break;
            }
            
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                System.out.println(selectedRegion);
                return result;
            }

            //Find available first and last year
            query = "SELECT MIN(Year) AS Min, MAX(Year) AS Max";

            switch(region){
                case 1:
                    query = query + " FROM GlobalYearlyLandTempByCountry WHERE Country = '" + selectedRegion + "'";
                    break;
                case 2:
                    query = query + " FROM GlobalYearlyLandTempByState WHERE State = '" + selectedRegion + "'";
                    break;
                case 3:
                    query = query + " FROM GlobalYearlyLandTempByCity WHERE City = '" + selectedRegion + "'";
                    break;
            }

            int beginYear = 0;
            int endYear = 0;
            ResultSet results = statement.executeQuery(query);
            if (results.next()){
                beginYear = results.getInt("Min");
                endYear = results.getInt("Max");
            }
            statement.close();


            //Find average temperature of the selected region in a certain time period
            query = "SELECT AVG(AverageTemperature) AS Avg ";

            switch(region){
                case 1:
                    query = query + "FROM GlobalYearlyLandTempByCountry WHERE Country = '" + selectedRegion + "' AND (Year BETWEEN'" + startingYear + "' AND '" + (startingYear + timePeriod - 1) + "')";
                    break;
                case 2:
                    query = query + "FROM GlobalYearlyLandTempByState WHERE State = '" + selectedRegion + "' AND (Year BETWEEN'" + startingYear + "' AND '" + (startingYear + timePeriod - 1) + "')";
                    break;
                case 3:
                    query = query + "FROM GlobalYearlyLandTempByCity WHERE City = '" + selectedRegion + "' AND (Year BETWEEN'" + startingYear + "' AND '" + (startingYear + timePeriod - 1) + "')";
                    break;
            }

            results = statement.executeQuery(query);
            double avg = 0;
            while (results.next()) {
                avg = results.getDouble("Avg");
            }

            statement.close();

            //Find average temperature of the selected region in various time period
            double average;
            double similarRate;
            for(int i = beginYear; i <= endYear - (timePeriod - 1); i++){
                average = 0;
                query = "SELECT AVG(AverageTemperature) AS Avg ";

                switch(region){
                    case 1:
                        query = query + "FROM GlobalYearlyLandTempByCountry WHERE Country = '" + selectedRegion + "' AND (Year BETWEEN'" + i + "' AND '" + (i + timePeriod - 1) + "')";
                        break;
                    case 2:
                        query = query + "FROM GlobalYearlyLandTempByState WHERE State = '" + selectedRegion + "' AND (Year BETWEEN'" + i + "' AND '" + (i + timePeriod - 1) + "')";
                        break;
                    case 3:
                        query = query + "FROM GlobalYearlyLandTempByCity WHERE City = '" + selectedRegion + "' AND (Year BETWEEN'" + i + "' AND '" + (i + timePeriod - 1) + "')";
                        break;
                }
                results = statement.executeQuery(query);
                while (results.next()) {
                    average = results.getDouble("Avg");
                }
                if (mode == 1){
                    similarRate = Math.abs(average - avg);
                }
                else{
                    similarRate = Math.abs((average - avg) / avg) * 100;
                }
                SubTaskB temp = new SubTaskB(i, i + timePeriod - 1, average, 0, similarRate, selectedRegion, region);
                result.add(temp);
                statement.close();
            }


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return result;
    }

    public static ArrayList<SubTaskB> similarPopulation (int startingYear, int timePeriod, String selectedRegion, int mode){
        ArrayList<SubTaskB> result = new ArrayList<>();
        
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE3);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the country exists
            String query = "SELECT EXISTS (SELECT 1 FROM Population WHERE Country_Name = '" + selectedRegion + "' AND (Year BETWEEN'" + startingYear + "' AND '" + (startingYear + timePeriod - 1) + "'))";
            
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                return result;
            }

            //Find available first and last year
            query = "SELECT MIN(Year) AS Min, MAX(Year) AS Max FROM Population WHERE Country_Name = '" + selectedRegion + "'";

            int beginYear = 0;
            int endYear = 0;
            ResultSet results = statement.executeQuery(query);
            if (results.next()){
                beginYear = results.getInt("Min");
                endYear = results.getInt("Max");
            }
            statement.close();


            //Find average population of the selected region in a certain time period
            query = "SELECT AVG(Population) AS Avg FROM Population WHERE Country_Name = '" + selectedRegion + "' AND (Year BETWEEN'" + startingYear + "' AND '" + (startingYear + timePeriod - 1) + "')";
            results = statement.executeQuery(query);
            double avg = 0;
            while (results.next()) {
                avg = results.getDouble("Avg");
            }

            statement.close();

            //Find average temperature of the selected region in various time period
            double average;
            double similarRate;
            for(int i = beginYear; i <= endYear - (timePeriod - 1); i++){
                average = 0;
                query = "SELECT AVG(Population) AS Avg FROM Population WHERE Country_Name = '" + selectedRegion + "' AND (Year BETWEEN'" + i + "' AND '" + (i + timePeriod - 1) + "')";
                results = statement.executeQuery(query);
                while (results.next()) {
                    average = results.getDouble("Avg");
                }
                if (mode == 1){
                    similarRate = Math.abs(average - avg);
                }
                else{
                    similarRate = Math.abs((average - avg) / avg) * 100;
                }
                SubTaskB temp = new SubTaskB(i, i + timePeriod - 1, 0, (int) average, similarRate, selectedRegion, 1);
                result.add(temp);
                statement.close();
            }


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return result;
    }

    public static ArrayList<SubTaskB> similarTempAndPopulation (int startingYear, int timePeriod, String selectedRegion, int mode){
        ArrayList<SubTaskB> result = new ArrayList<>();
        
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE3);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the country exists
            String query = "SELECT EXISTS (SELECT 1 FROM GlobalYearlyLandTempByCountry AS Temperature JOIN Population ON Temperature.Country = Population.Country_Name WHERE Temperature.Country = '" + selectedRegion + "'  AND (Population.Year BETWEEN'" + startingYear + "' AND '" + (startingYear + timePeriod - 1) + "'))";
            
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                return result;
            }

            //Find available first and last year
            query = "SELECT MIN(Year) AS Min, MAX(Year) AS Max FROM Population WHERE Country_Name = '" + selectedRegion + "' ";

            int beginYear = 0;
            int endYear = 0;
            ResultSet results = statement.executeQuery(query);
            if (results.next()){
                beginYear = results.getInt("Min");
                endYear = results.getInt("Max");
            }
            statement.close();


            //Find average population of the selected region in a certain time period
            query = "SELECT AVG(Population) AS Avg FROM Population WHERE Country_Name = '" + selectedRegion + "' AND (Year BETWEEN'" + startingYear + "' AND '" + (startingYear + timePeriod - 1) + "')";
            results = statement.executeQuery(query);
            double avgPopulation = 0;
            while (results.next()) {
                avgPopulation = results.getDouble("Avg");
            }

            statement.close();

            //Find average temperature of the selected region in a certain time period
            query = "SELECT AVG(AverageTemperature) AS Avg FROM GlobalYearlyLandTempByCountry WHERE Country = '" + selectedRegion + "' AND (Year BETWEEN'" + startingYear + "' AND '" + (startingYear + timePeriod - 1) + "')";
            results = statement.executeQuery(query);
            double avgTemp = 0;
            while (results.next()) {
                avgTemp = results.getDouble("Avg");
            }

            statement.close();

            //Find average temperature of the selected region in various time period
            double averageTemp;
            double averagePopulation;
            double similarRate;
            for(int i = beginYear; i <= endYear - (timePeriod - 1); i++){

                averageTemp = 0;
                averagePopulation = 0;

                query = "SELECT AVG(Population) AS Avg FROM Population WHERE Country_Name = '" + selectedRegion + "' AND (Year BETWEEN'" + i + "' AND '" + (i + timePeriod - 1) + "')";
                results = statement.executeQuery(query);
                while (results.next()) {
                    averagePopulation = results.getDouble("Avg");
                }

                query = "SELECT AVG(AverageTemperature) AS Avg FROM GlobalYearlyLandTempByCountry WHERE Country = '" + selectedRegion + "' AND (Year BETWEEN'" + i + "' AND '" + (i + timePeriod - 1) + "')";
                results = statement.executeQuery(query);
                while (results.next()) {
                    averageTemp = results.getDouble("Avg");
                }

                if (mode == 1){
                    similarRate = Math.sqrt(Math.pow(averageTemp - avgTemp, 2) + Math.pow(averagePopulation - avgPopulation, 2));
                }
                else{
                    similarRate = Math.sqrt(Math.pow(((averageTemp - avgTemp) / avgTemp) * 100, 2) + Math.pow(((averagePopulation - avgPopulation) / avgPopulation) * 100, 2));
                }
                SubTaskB temp = new SubTaskB(i, i + timePeriod - 1, averageTemp, (int) averagePopulation, similarRate, selectedRegion, 1);
                result.add(temp);
                statement.close();
            }


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return result;
    }

    public ArrayList<SubTaskB> subTaskBTask2 (int startingYear, int timePeriod, String selectedRegion, int region, int option, int mode, boolean mostSimilar, boolean omit){
    // region 1  = country, 2 = state, 3 = city
    // option 1 = temperature, 2 = population, 3 = both
    // mode 1 = absolute, 2 = relative
    ArrayList<SubTaskB> result = new ArrayList<SubTaskB>();
    switch(option){
        case 1:
            result = similarTempTask2(startingYear, timePeriod, selectedRegion, region, mode, mostSimilar, omit);
            break;
        case 2:
            if (region == 1){
                result = similarPopulationTask2(startingYear, timePeriod, selectedRegion, mode, mostSimilar, omit);
                break;
            }
            else{
                break;
            }
        case 3:
            if (region == 1){
                result = similarTempAndPopulationTask2(startingYear, timePeriod, selectedRegion, mode, mostSimilar, omit);
                break;
            }
            else{
                break;
            }
    }

    if(mostSimilar){
        SortLowToHigh(result);
    }
    else{
        SortHighToLow(result);
    }
    
    return result;
}

    public static ArrayList<SubTaskB> similarTempTask2 (int startingYear, int timePeriod, String selectedRegion, int region, int mode, boolean mostSimilar, boolean omit){
    ArrayList<SubTaskB> result = new ArrayList<>();
        
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE3);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the region exists
            String query = "SELECT EXISTS";

            switch(region){
                case 1:
                    query += " (SELECT 1 FROM GlobalYearlyLandTempByCountry WHERE Country = ? AND (Year BETWEEN ? AND ?))";
                    break;
                case 2:
                    query += " (SELECT 1 FROM GlobalYearlyLandTempByState WHERE State = ? AND (Year BETWEEN ? AND ?))";
                    break;
                case 3:
                    query += " (SELECT 1 FROM GlobalYearlyLandTempByCity WHERE City = ? AND (Year BETWEEN ? AND ?))";
                    break;
            }

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod - 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                System.out.println(selectedRegion);
                return result;
            }

            //Find average temperature of the selected region in a certain time period
            query = "SELECT AVG(AverageTemperature) AS Avg ";

            switch(region){
                case 1:
                    query += "FROM GlobalYearlyLandTempByCountry WHERE Country = ? AND (Year BETWEEN ? AND ?)";
                    break;
                case 2:
                    query += "FROM GlobalYearlyLandTempByState WHERE State = ? AND (Year BETWEEN ? AND ?)";
                    break;
                case 3:
                    query += "FROM GlobalYearlyLandTempByCity WHERE City = ? AND (Year BETWEEN ? AND ?)";
                    break;
            }
            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod - 1);
            ResultSet results = preparedStatement.executeQuery();

            double avg = 0;
            while (results.next()) {
                avg = results.getDouble("Avg");
            }

            statement.close();

            //Retrieve a list of all regions
            switch(region){
                case 1:
                    query = "SELECT DISTINCT Country FROM GlobalYearlyLandTempByCountry";
                    break;
                case 2:
                    query = "SELECT DISTINCT State FROM GlobalYearlyLandTempByState";
                    break;
                case 3:
                    query = "SELECT DISTINCT City FROM GlobalYearlyLandTempByCity";
                    break;
            }

            results = statement.executeQuery(query);
            ArrayList<String> data = new ArrayList<>();
            while (results.next()) {
                switch(region){
                    case 1:
                        data.add(results.getString("Country"));
                        break;
                    case 2:
                        data.add(results.getString("State"));
                        break;
                    case 3:
                        data.add(results.getString("City"));
                        break;
                }
            }

            statement.close();



            //Going through every region and find the average temperature in a certain time period
            int beginYear = 0;
            int endYear = 0;

            for(int x = 0; x < data.size(); x++){
                ArrayList<SubTaskB> avgTemp = new ArrayList<>();
                double average;
                double similarRate;
                String currentRegion = data.get(x);

                //Find available first and last year
                query = "SELECT MIN(Year) AS Min, MAX(Year) AS Max FROM ";

                switch(region){
                    case 1:
                        query += "GlobalYearlyLandTempByCountry WHERE Country = ?";
                        break;
                    case 2:
                        query += "GlobalYearlyLandTempByState WHERE State = ?";
                        break;
                    case 3:
                        query += "GlobalYearlyLandTempByCity WHERE City = ?";
                        break;
                }

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, currentRegion);
                results = preparedStatement.executeQuery();

                results = preparedStatement.executeQuery();
                if (results.next()){
                    beginYear = results.getInt("Min");
                    endYear = results.getInt("Max");
                }
                preparedStatement.close();

                //Find average temperature of the selected region in various time period and then get the highest similar rate
                for(int i = beginYear; i <= endYear - (timePeriod - 1); i++){
                    average = 0;
                    
                    query = "SELECT AVG(AverageTemperature) AS Avg FROM ";

                    switch(region){
                        case 1:
                            query += "GlobalYearlyLandTempByCountry WHERE Country = ? AND (Year BETWEEN ? AND ?)";
                            break;
                        case 2:
                            query += "GlobalYearlyLandTempByState WHERE State = ? AND (Year BETWEEN ? AND ?)";
                            break;
                        case 3:
                            query += "GlobalYearlyLandTempByCity WHERE City = ? AND (Year BETWEEN ? AND ?)";
                            break;
                    }

                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, currentRegion);
                    preparedStatement.setInt(2, i);
                    preparedStatement.setInt(3, i + timePeriod - 1);
                    results = preparedStatement.executeQuery();


                    while (results.next()) {
                        average = results.getDouble("Avg");
                    }

                    // Absolute or relative
                    if (mode == 1){
                        similarRate = Math.abs(average - avg) ;
                    }
                    else{
                        similarRate = Math.abs((average - avg) / avg) * 100;
                    }

                    SubTaskB temp = new SubTaskB(i, i + timePeriod - 1, average, 0, similarRate, currentRegion, region);
                    avgTemp.add(temp);
                    statement.close();
                }

                if(mostSimilar){
                    SortLowToHigh(avgTemp);
                }
                else{
                    SortHighToLow(avgTemp);
                }
                if(omit){
                result.add(avgTemp.get(0));
                } else {
                    result.addAll(avgTemp);
                }
            }


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    

    return result;
}

    public static ArrayList<SubTaskB> similarPopulationTask2 (int startingYear, int timePeriod, String selectedRegion, int mode, boolean mostSimilar, boolean omit){
    ArrayList<SubTaskB> result = new ArrayList<>();
        
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE3);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the region exists
            String query = "SELECT EXISTS (SELECT 1 FROM Population WHERE Country_Name = ? AND (Year BETWEEN ? AND ?))";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod - 1);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                System.out.println(selectedRegion);
                return result;
            }
            preparedStatement.close();


            //Find average temperature of the selected region in a certain time period
            query = "SELECT AVG(Population) AS Avg FROM Population WHERE Country_Name = ? AND (Year BETWEEN ? AND ?)";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod - 1);

            ResultSet results = preparedStatement.executeQuery();
            double avg = 0;
            while (results.next()) {
                avg = results.getDouble("Avg");
            }

            preparedStatement.close();

            //Retrieve a list of all other regions besides the selected one
            query = "SELECT DISTINCT Country_Name AS Country FROM Population";

            results = statement.executeQuery(query);
            ArrayList<String> data = new ArrayList<>();
            while (results.next()) {
                    data.add(results.getString("Country"));
            }

            statement.close();



            //Going through every region and find the average population in a certain time period
            int beginYear = 0;
            int endYear = 0;

            for(int x = 0; x < data.size(); x++){
                ArrayList<SubTaskB> avgTemp = new ArrayList<>();
                double average;
                double similarRate;
                String currentRegion = data.get(x);

                //Find available first and last year
                query = "SELECT MIN(Year) AS Min, MAX(Year) AS Max FROM Population WHERE Country_Name = ?";

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, currentRegion);
                results = preparedStatement.executeQuery();

                if (results.next()){
                    beginYear = results.getInt("Min");
                    endYear = results.getInt("Max");
                }
                preparedStatement.close();

                //Find average population of the selected region in various time period and then get the highest similar rate
                for(int i = beginYear; i <= endYear - (timePeriod - 1); i++){
                    average = 0;


                    query = "SELECT AVG(Population) AS Avg FROM Population WHERE Country_Name = ? AND (Year BETWEEN ? AND ?)";

                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, currentRegion);
                    preparedStatement.setInt(2, i);
                    preparedStatement.setInt(3, i + timePeriod - 1);
                    results = preparedStatement.executeQuery();

                    if(results.next()) {
                        average = results.getDouble("Avg");
                    }

                    results.close();

                    // Absolute or relative
                    if (mode == 1){
                        similarRate = Math.abs(average - avg) ;
                    }
                    else{
                        similarRate = Math.abs((average - avg) / avg) * 100;
                    }

                    SubTaskB temp = new SubTaskB(i, i + timePeriod - 1, 0, (int) average, similarRate, currentRegion, 1);
                    avgTemp.add(temp);

                    preparedStatement.close();

                }

                if(mostSimilar){
                    SortLowToHigh(avgTemp);
                }
                else{
                    SortHighToLow(avgTemp);
                }

                if(omit){
                    result.add(avgTemp.get(0));
                } else {
                    result.addAll(avgTemp);
                }
            }


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    

    return result;
}

    public static ArrayList<SubTaskB> similarTempAndPopulationTask2 (int startingYear, int timePeriod, String selectedRegion, int mode, boolean mostSimilar, boolean omit){
    ArrayList<SubTaskB> result = new ArrayList<>();
        
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE3);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the region exists
            String query = "SELECT EXISTS (SELECT 1 FROM GlobalYearlyLandTempByCountry AS Temperature JOIN Population ON Temperature.Country = Population.Country_Name WHERE Temperature.Country = ? AND (Population.Year BETWEEN ? AND ?))";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod - 1);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                return result;
            }
            
            preparedStatement.close();

            // Prepare and execute the first query
            String query1 = "SELECT AVG(Population.Population) AS AvgPopulation " +
            "FROM Population " +
            "WHERE Population.Country_Name = ? AND (Population.Year BETWEEN ? AND ?)";

            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setString(1, selectedRegion);
            preparedStatement1.setInt(2, startingYear);
            preparedStatement1.setInt(3, startingYear + timePeriod - 1);
            ResultSet results1 = preparedStatement1.executeQuery();

            double avgPopulation = 0;
            if (results1.next()) {
            avgPopulation = results1.getDouble("AvgPopulation");
            }
            preparedStatement1.close();

            // Prepare and execute the second query
            String query2 = "SELECT AVG(Temp.AverageTemperature) AS AvgTemp " +
            "FROM GlobalYearlyLandTempByCountry AS Temp " +
            "WHERE Temp.Country = ? AND (Temp.Year BETWEEN ? AND ?)";

            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setString(1, selectedRegion);
            preparedStatement2.setInt(2, startingYear);
            preparedStatement2.setInt(3, startingYear + timePeriod - 1);
            ResultSet results2 = preparedStatement2.executeQuery();

            double avgTemp = 0;
            if (results2.next()) {
            avgTemp = results2.getDouble("AvgTemp");
            }
            preparedStatement2.close();

            //Retrieve a list of all other regions besides the selected one

            query = "SELECT DISTINCT Population.Country_Name AS Country " +
               "FROM Population " +
               "JOIN GlobalYearlyLandTempByCountry ON Population.Country_Name = GlobalYearlyLandTempByCountry.Country";

            preparedStatement = connection.prepareStatement(query);
            ResultSet results = preparedStatement.executeQuery();

            ArrayList<String> data = new ArrayList<>();
            while (results.next()) {
                data.add(results.getString("Country"));
            }

            preparedStatement.close();

            //Going through every region and find the average temperature and population in a certain time period

            query1 = "SELECT AVG(Population.Population) AS AvgPopulation " +
                                "FROM Population " +
                                "WHERE Population.Country_Name = ? AND (Population.Year BETWEEN ? AND ?)";


            query2 = "SELECT AVG(Temp.AverageTemperature) AS AvgTemp " +
                                "FROM GlobalYearlyLandTempByCountry AS Temp " +
                                "WHERE Temp.Country = ? AND (Temp.Year BETWEEN ? AND ?)";
                        
            preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement2 = connection.prepareStatement(query2);
            
            for(int x = 0; x < data.size(); x++){
                String currentRegion = data.get(x);
                ArrayList<SubTaskB> tempList = new ArrayList<>();
                double averageTemp = 0;
                double averagePopulation = 0;
                double differenceScore = 0;

                preparedStatement1.setString(1, currentRegion);
                preparedStatement2.setString(1, currentRegion);

                //Find available first and last year

                query = "SELECT MIN(Year) AS Min, MAX(Year) AS Max FROM Population WHERE Country_Name = ?";
                int beginYear = 0;
                int endYear = 0;

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, selectedRegion);
                results = preparedStatement.executeQuery();

                if (results.next()){
                    beginYear = results.getInt("Min");
                    endYear = results.getInt("Max");
                }
                preparedStatement.close();

                //Find average population and temperature of the selected region in various time period and then get the highest similar rate
                for(int i = beginYear; i <= endYear - (timePeriod - 1); i++){
                    
                    averageTemp = 0;
                    averagePopulation = 0;

                    preparedStatement1.setInt(2, i);
                    preparedStatement1.setInt(3, i + timePeriod - 1);

                    preparedStatement2.setInt(2, i);
                    preparedStatement2.setInt(3, i + timePeriod - 1);

                    results1 = preparedStatement1.executeQuery();


                    if (results1.next()) {
                        averagePopulation = results1.getDouble("AvgPopulation");
                    }

                    results2 = preparedStatement2.executeQuery();

                    if (results2.next()) {
                        averageTemp = results2.getDouble("AvgTemp");
                    }
                    
                    if (mode == 1){
                        differenceScore = Math.sqrt((averageTemp - avgTemp) * (averageTemp - avgTemp) + (averagePopulation - avgPopulation) * (averagePopulation - avgPopulation));
                    }
                    else {
                        differenceScore = Math.sqrt(Math.pow(((averageTemp - avgTemp) / avgTemp) * 100, 2) + Math.pow(((averagePopulation - avgPopulation) / avgPopulation) * 100, 2));
                    }

                    SubTaskB temp = new SubTaskB(i, i + timePeriod - 1, averageTemp, (int) averagePopulation, differenceScore, currentRegion, 1);
                    tempList.add(temp);

                }

                if(mostSimilar){
                    SortLowToHigh(tempList);
                }
                else{
                    SortHighToLow(tempList);
                }

                if(omit){
                    result.add(tempList.get(0));
                } else {
                    result.addAll(tempList);
                }
            }


            preparedStatement1.close();
            preparedStatement2.close();


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    

    return result;
}

    public static ArrayList<SubTaskB> SortHighToLow (ArrayList<SubTaskB> data){
    
    Collections.sort(data, new Comparator<SubTaskB>() {
        @Override
        public int compare(SubTaskB o1, SubTaskB o2) {
            return Double.compare(o2.getDifferenceScore(), o1.getDifferenceScore());
        }
    });

    return data;
}

    public static ArrayList<SubTaskB> SortLowToHigh (ArrayList<SubTaskB> data){
    
    Collections.sort(data, new Comparator<SubTaskB>() {
        @Override
        public int compare(SubTaskB o1, SubTaskB o2) {
            return Double.compare(o1.getDifferenceScore(), o2.getDifferenceScore());
        }
    });

    return data;
}

    public static ArrayList<SubTaskB> Reverse (ArrayList<SubTaskB> data){
    
    Collections.reverse(data);

    return data;
}

    public ArrayList<SubTaskB> publicSortHighToLow (ArrayList<SubTaskB> data){
    
    Collections.sort(data, new Comparator<SubTaskB>() {
        @Override
        public int compare(SubTaskB o1, SubTaskB o2) {
            return Double.compare(o2.getDifferenceScore(), o1.getDifferenceScore());
        }
    });

    return data;
}

    public ArrayList<SubTaskB> publicSortLowToHigh (ArrayList<SubTaskB> data){
    
    Collections.sort(data, new Comparator<SubTaskB>() {
        @Override
        public int compare(SubTaskB o1, SubTaskB o2) {
            return Double.compare(o1.getDifferenceScore(), o2.getDifferenceScore());
        }
    });

    return data;
}

    public ArrayList<SubTaskB> publicReverse (ArrayList<SubTaskB> data){
    
    Collections.reverse(data);

    return data;
}


    // Subtask A


    public ArrayList<SubTaskA> SubTaskATask4 (int[] startingYears, int timePeriod, String[] selectedRegions, int region){
        ArrayList<SubTaskA> result = new ArrayList<SubTaskA>();

        for(String selectedRegion : selectedRegions){
            for(int startingYear : startingYears){
                SubTaskA temp = differenceInAverage(startingYear, timePeriod, selectedRegion, region);
                result.add(temp);
            }
        }

        return result;
    }

    public ArrayList<SubTaskA> SubTaskATask3 (int[] startingYears, int timePeriod, String selectedRegion, int region){
        ArrayList<SubTaskA> result = new ArrayList<SubTaskA>();

        for(int startingYear : startingYears){
            SubTaskA temp = differenceInAverage(startingYear, timePeriod, selectedRegion, region);
            result.add(temp);
        }

        return result;
    }

    public ArrayList<String> SubTaskATask6 (double startValue, double endValue, int mode){
        ArrayList<String> data = new ArrayList<>();

        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE3);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "";
            switch(mode){
                case 1:
                    query = "SELECT DISTINCT Country FROM GlobalYearlyLandTempByCountry WHERE AverageTemperature BETWEEN ? AND ? AND YEAR = 2013";
                    break;
                case 2:
                    query = "SELECT DISTINCT Country FROM Population WHERE Population BETWEEN ? AND ? AND YEAR = 2013";
                    break;
            }    

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, startValue);
            preparedStatement.setDouble(2, endValue);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add(resultSet.getString("Country"));
            }
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return data;
    }

    public static SubTaskA differenceInAverage (int startingYear, int timePeriod, String selectedRegion, int region){
        SubTaskA result = null;
        
        Connection connection = null;

        if (timePeriod == 0){
            System.out.println("Invalid time period");
            return result;
        }
        
        try{
            connection = DriverManager.getConnection(DATABASE3);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the country exists
            String query = "SELECT EXISTS (SELECT 1 FROM ";

            switch(region){
                case 0:
                    query += "GlobalYearlyLandTemp WHERE Year BETWEEN ? AND ?)";
                    break;
                case 1:
                    query += "GlobalYearlyLandTempByCountry WHERE Country = ? AND Year BETWEEN ? AND ?)";
                    break;
                case 2:
                    query += "GlobalYearlyLandTempByState WHERE State = ? AND Year BETWEEN ? AND ?)";
                    break;
                case 3:
                    query += "GlobalYearlyLandTempByCity WHERE City = ? AND Year BETWEEN ? AND ?)";
                    break;
            }  

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            if(region != 0){
                preparedStatement.setString(1, selectedRegion);
                preparedStatement.setInt(2, startingYear);
                preparedStatement.setInt(3, startingYear + timePeriod - 1);
            } else {
                preparedStatement.setInt(1, startingYear);
                preparedStatement.setInt(2, startingYear + timePeriod - 1);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                System.out.println(selectedRegion);
                return result;
            }

            preparedStatement.close();

            //Find average temperature of the selected region in a certain time period
            query = "SELECT AVG(AverageTemperature) AS Avg FROM ";

            switch(region){
                case 0:
                    query += "GlobalYearlyLandTemp WHERE Year BETWEEN ? AND ?";
                    break;
                case 1:
                    query += "GlobalYearlyLandTempByCountry WHERE Country = ? AND Year BETWEEN ? AND ?";
                    break;
                case 2:
                    query += "GlobalYearlyLandTempByState WHERE State = ? AND Year BETWEEN ? AND ?";
                    break;
                case 3:
                    query += "GlobalYearlyLandTempByCity WHERE City = ? AND Year BETWEEN ? AND ?";
                    break;
            }

            preparedStatement = connection.prepareStatement(query);

            if(region != 0){
                preparedStatement.setString(1, selectedRegion);
                preparedStatement.setInt(2, startingYear);
                preparedStatement.setInt(3, startingYear + timePeriod - 1);
            } else {
                preparedStatement.setInt(1, startingYear);
                preparedStatement.setInt(2, startingYear + timePeriod - 1);
            }

            resultSet = preparedStatement.executeQuery();
            double avg = 0;
            while (resultSet.next()) {
                avg = resultSet.getDouble("Avg");
            }

            preparedStatement.close();

            //Find difference in temperature of the selected region
            query = "SELECT MinimumTemperature AS Min, MaximumTemperature AS Max FROM ";
            switch(region){
                case 0:
                    query += "GlobalYearlyLandTemp WHERE Year BETWEEN ? AND ?";
                    break;
                case 1:
                    query += "GlobalYearlyLandTempByCountry WHERE Country = ? AND Year BETWEEN ? AND ?";
                    break;
                case 2:
                    query += "GlobalYearlyLandTempByState WHERE State = ? AND Year BETWEEN ? AND ?";
                    break;
                case 3:
                    query += "GlobalYearlyLandTempByCity WHERE City = ? AND Year BETWEEN ? AND ?";
                    break;
            }

            preparedStatement = connection.prepareStatement(query);

            if(region != 0){
                preparedStatement.setString(1, selectedRegion);
                preparedStatement.setInt(2, startingYear);
                preparedStatement.setInt(3, startingYear + timePeriod - 1);
            } else {
                preparedStatement.setInt(1, startingYear);
                preparedStatement.setInt(2, startingYear + timePeriod - 1);
            }
            
            resultSet = preparedStatement.executeQuery();
            double min = 0;
            double max = 0;
            double minTotal = 0;
            double maxTotal = 0;
            while (resultSet.next()) {
                min = resultSet.getDouble("Min");
                max = resultSet.getDouble("Max");
                minTotal += min;
                maxTotal += max;
            }
            double averageTempDifference = Math.abs((maxTotal - minTotal) / timePeriod);

            preparedStatement.close();

            result = new SubTaskA(startingYear, startingYear + timePeriod - 1, avg, averageTempDifference, selectedRegion, region);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        
        return result;
    }

    public ArrayList<SubTaskA> TaskASortLowToHighATD (ArrayList<SubTaskA> data){
    
        Collections.sort(data, new Comparator<SubTaskA>() {
            @Override
            public int compare(SubTaskA o1, SubTaskA o2) {
                return Double.compare(o1.getAverageTempDifference(), o2.getAverageTempDifference());
            }
        });
    
        return data;
    }

    public ArrayList<SubTaskA> TaskASortHighToLowATD (ArrayList<SubTaskA> data){
    
        Collections.sort(data, new Comparator<SubTaskA>() {
            @Override
            public int compare(SubTaskA o1, SubTaskA o2) {
                return Double.compare(o2.getAverageTempDifference(), o1.getAverageTempDifference());
            }
        });
    
        return data;
    }
}