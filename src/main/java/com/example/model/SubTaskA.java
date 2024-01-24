package com.example.model;

public class SubTaskA {
    private int startingYear;
    private int endingYear;
    private double averageTemp;
    private double averageTempDifference;
    private String regionName;
    private int type;

    public SubTaskA(){
    }

    public SubTaskA (int startingYear, int endingYear, double averageTemp, double averageTempDifference, String regionName, int type){
        this.startingYear = startingYear;
        this.endingYear = endingYear;
        this.averageTemp = averageTemp;
        this.averageTempDifference = averageTempDifference;
        this.regionName = regionName;
        this.type = type;
    }

    public double getAverageTemp(){
        return averageTemp;
    }

    public int getStartingYear(){
        return startingYear;
    }

    public int getEndingYear(){
        return endingYear;
    }

    public double getAverageTempDifference(){
        return averageTempDifference;
    }

    public String getRegionName(){
        return regionName;
    }

    public int getType(){
        return type;
    }

    public void setAverageTemp(double averageTemp){
        this.averageTemp = averageTemp;
    }

    public void setStartingYear(int startingYear){
        this.startingYear = startingYear;
    }

    public void setEndingYear(int endingYear){
        this.endingYear = endingYear;
    }

    public void setAverageTempDifference(double averageTempDifference){
        this.averageTempDifference = averageTempDifference;
    }

    public void setRegionName(String regionName){
        this.regionName = regionName;
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    public String toString() {
        return  "Region Name: " + regionName +
                "\nType: " + type +
                "\nStarting year: " + startingYear + 
                "\nEnding Year: " + endingYear + 
                "\nAverage Temperature: " + averageTemp +
                "\nAverage Temperature DIfference: " + averageTempDifference + "\n\n";
    }
}
