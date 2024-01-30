package com.example.model;
import java.text.DecimalFormat;

public class SubTaskA {
    private int startingYear;
    private int endingYear;
    private int timePeriod;
    private double averageTemp;
    private long averagePopulation;
    private double averageTempDifference;
    private String regionName;
    private int type;
    private double fromValue;
    private double toValue;

    DecimalFormat df = new DecimalFormat("#,###.##");

    public SubTaskA(){
    }

    public SubTaskA (int startingYear, int endingYear, int timePeriod, double averageTemp, double averageTempDifference, long averagePopulation, String regionName, int type){
        this.startingYear = startingYear;
        this.endingYear = endingYear;
        this.timePeriod = timePeriod;
        this.averageTemp = averageTemp;
        this.averageTempDifference = averageTempDifference;
        this.averagePopulation = averagePopulation;
        this.regionName = regionName;

        this.type = type;
    }

    public SubTaskA (double averageTemp, long averagePopulation, String regionName, int type, double fromValue, double toValue){
        this.averageTemp = averageTemp;
        this.averagePopulation = averagePopulation;
        this.regionName = regionName;
        this.type = type;
        this.fromValue = fromValue;
        this.toValue = toValue;
    }

    public double getFromValue() {
        return fromValue;
    }

    public void setFromValue(double fromValue) {
        this.fromValue = fromValue;
    }

    public double getToValue() {
        return toValue;
    }

    public void setToValue(double toValue) {
        this.toValue = toValue;
    }

    public long getAveragePopulation() {
        return averagePopulation;
    }

    public String getAveragePopulationString(){
        return df.format(averagePopulation).replace(',', '.');
    }

    public void setAveragePopulation(long averagePopulation) {
        this.averagePopulation = averagePopulation;
    }

    public double getAverageTemp(){
        return averageTemp;
    }

    public String getAverageTempString(){
        return df.format(averageTemp).replace(',', '.');
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

    public String getAverageTempDifferenceString(){
        return df.format(averageTempDifference).replace(',', '.');
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

    public int getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }

    @Override
    public String toString() {
        return  "Region Name: " + regionName +
                "\nType: " + type +
                "\nStarting year: " + startingYear + 
                "\nEnding Year: " + endingYear + 
                "\nTime Period: " + timePeriod +
                "\nfromValue: " + fromValue +
                "\ntoValue: " + toValue +
                "\nAverage Population: " + averagePopulation +
                "\nAverage Temperature: " + averageTemp +
                "\nAverage Temperature difference: " + averageTempDifference + "\n\n";
    }
}
