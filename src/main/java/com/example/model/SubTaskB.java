package com.example.model;

public class SubTaskB {
    private int startingYear;
    private int endingYear;
    private double temp;
    private int population;
    private double similarRate;
    private String regionName;
    private int type;

    public SubTaskB(){
    }

    public SubTaskB (int startingYear, int endingYear, double avgTemp, int population, double similarRate, String regionName, int type){
        this.startingYear = startingYear;
        this.endingYear = endingYear;
        this.temp = avgTemp;
        this.population = population;
        this.similarRate = similarRate;
        this.regionName = regionName;
        this.type = type;
    }

    public double getTemp(){
        return temp;
    }

    public int getStartingYear(){
        return startingYear;
    }

    public int getEndingYear(){
        return endingYear;
    }

    public double getSimilarRate(){
        return similarRate;
    }

    public int getPopulation(){
        return population;
    }

    public String getRegionName(){
        return regionName;
    }

    public int getType(){
        return type;
    }

    public void setTemp(double temp){
        this.temp = temp;
    }

    public void setStartingYear(int startingYear){
        this.startingYear = startingYear;
    }

    public void setEndingYear(int endingYear){
        this.endingYear = endingYear;
    }

    public void setSimilarRate(double similarRate){
        this.similarRate = similarRate;
    }

    public void setPopulation(int avgPopulation){
        this.population = avgPopulation;
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
                "\nAverageTemperature: " + temp + 
                "\nAverage Population: " + population + 
                "\nSimilar Rate: " + similarRate + "\n\n";
    }
}
