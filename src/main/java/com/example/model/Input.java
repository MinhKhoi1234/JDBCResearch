package com.example.model;

public class Input {

  private String countryName;
  private long startYear;
  private long endYear;
  private String order;
  private String sortBy;
  private String cityState;
  private String type;

  public String getCountryName() {
    return countryName;
  }
  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }
  public long getStartYear() {
    return startYear;
  }
  public void setStartYear(long startYear) {
    this.startYear = startYear;
  }
  public long getEndYear() {
    return endYear;
  }
  public void setEndYear(long endYear) {
    this.endYear = endYear;
  }
  public String getOrder() {
    return order;
  }
  public void setOrder(String order) {
    this.order = order;
  }
  public String getSortBy() {
    return sortBy;
  }
  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }
  public String getCityState() {
    return cityState;
  }
  public void setCityState(String cityState) {
    this.cityState = cityState;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  private String city;
  private double avgTemp;
  private double minTemp;
  private double maxTemp;

  public String getCity() {
      return city;
  }

  public void setCity(String city) {
      this.city = city;
  }

  public double getAvgTemp() {
      return avgTemp;
  }

  public void setAvgTemp(double avgTemp) {
      this.avgTemp = avgTemp;
  }

  public double getMinTemp() {
      return minTemp;
  }

  public void setMinTemp(double minTemp) {
      this.minTemp = minTemp;
  }

  public double getMaxTemp() {
      return maxTemp;
  }

  public void setMaxTemp(double maxTemp) {
      this.maxTemp = maxTemp;
  }

}