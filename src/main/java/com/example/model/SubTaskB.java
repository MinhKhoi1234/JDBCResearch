package com.example.model;

public class SubTaskB {
    private int startingYear;
    private int endingYear;
    private double avgTemp;
    private int population;
    private double similarRate;

    public SubTaskB(){
    }

    public SubTaskB (int startingYear, int endingYear, double avgTemp, int population, double similarRate){
        this.startingYear = startingYear;
        this.endingYear = endingYear;
        this.avgTemp = avgTemp;
        this.population = population;
        this.similarRate = similarRate;
    }

    public double getAvgTemp(){
        return avgTemp;
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

    public double getPopulation(){
        return population;
    }

    public void setAvgTemp(double avgTemp){
        this.avgTemp = avgTemp;
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

    @Override
    public String toString() {
        return "Starting year: " + startingYear + 
               "\nEnding Year: " + endingYear + 
               "\nAverageTemperature: " + avgTemp + 
               "\nAverage Population: " + population + 
               "\nSimilar Rate: " + similarRate + "\n\n";
    }
}
