package controllers

import (
	"encoding/json"
	"fmt"
	"github.com/jinzhu/gorm"
	_ "github.com/jinzhu/gorm/dialects/sqlite"
	"github.com/labstack/echo/v4"
	"log"
	"main/models"
	"net/http"
	"time"
)

type WeatherController struct {
	Token string
	DB    *gorm.DB
}

type WeatherResponse struct {
	Name    string    `json:"name"`
	Sys     Sys       `json:"sys"`
	Weather []Weather `json:"weather"`
	Main    Main      `json:"main"`
}

type Sys struct {
	Sunrise int64 `json:"sunrise"`
	Sunset  int64 `json:"sunset"`
}

type Weather struct {
	Main        string `json:"main"`
	Description string `json:"description"`
}

type Main struct {
	Temp      float32 `json:"temp"`
	FeelsLike float32 `json:"feels_like"`
	TempMin   float32 `json:"temp_min"`
	TempMax   float32 `json:"temp_max"`
	Pressure  int     `json:"pressure"`
	Humidity  int     `json:"humidity"`
}

// NewWeatherController
func NewWeatherController(token string, db *gorm.DB) *WeatherController {
	return &WeatherController{Token: token, DB: db}
}

func (w *WeatherController) GetWeatherForecast(c echo.Context) error {
	city := c.Param("city")
	url := fmt.Sprintf("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric", city, w.Token)

	forecast, saved, cityInDb := checkIfValidForecast(city, w.DB)
	if saved {
		return c.JSON(http.StatusOK, forecast)
	}

	// pobieranie danych
	resp, err := http.Get(url)
	if err != nil {
		log.Fatal(err)
	}
	defer resp.Body.Close()

	var weatherData WeatherResponse
	if err := json.NewDecoder(resp.Body).Decode(&weatherData); err != nil {
		log.Fatal(err)
	}

	if weatherData.Name == "" {
		return c.String(http.StatusNotFound, "City not found")
	}
	if !cityInDb {
		saveForecast(city, weatherData, w.DB)
	} else {
		err = updateCachedForecast(city, weatherData, w.DB)
		if err != nil {
			log.Fatal("Failed to update cached forecast.")
		}
	}
	log.Println("Returning fresh data from API")
	return c.JSON(http.StatusOK, convertResponseToModel(city, weatherData))
}

func convertResponseToModel(city string, weatherData WeatherResponse) models.Weather {
	var weatherRecord models.Weather
	weatherRecord.Name = city
	weatherRecord.Sunrise = convertUnixToDate(weatherData.Sys.Sunrise)
	weatherRecord.Sunset = convertUnixToDate(weatherData.Sys.Sunset)
	weatherRecord.Weather = weatherData.Weather[0].Main
	weatherRecord.Description = weatherData.Weather[0].Description
	weatherRecord.Temp = weatherData.Main.Temp
	weatherRecord.FeelsLike = weatherData.Main.FeelsLike
	weatherRecord.TempMin = weatherData.Main.TempMin
	weatherRecord.TempMax = weatherData.Main.TempMax
	weatherRecord.Pressure = weatherData.Main.Pressure
	weatherRecord.Humidity = weatherData.Main.Humidity
	weatherRecord.UpdatedAt = time.Now()

	return weatherRecord
}

func updateCachedForecast(city string, weatherData WeatherResponse, db *gorm.DB) error {
	var weatherRecord models.Weather
	if err := db.Where("name = ?", city).First(&weatherRecord).Error; err != nil {
		log.Fatal("Error checking weather in database:", err)
		return err
	}

	weatherRecord.Sunrise = convertUnixToDate(weatherData.Sys.Sunrise)
	weatherRecord.Sunset = convertUnixToDate(weatherData.Sys.Sunset)
	weatherRecord.Weather = weatherData.Weather[0].Main
	weatherRecord.Description = weatherData.Weather[0].Description
	weatherRecord.Temp = weatherData.Main.Temp
	weatherRecord.FeelsLike = weatherData.Main.FeelsLike
	weatherRecord.TempMin = weatherData.Main.TempMin
	weatherRecord.TempMax = weatherData.Main.TempMax
	weatherRecord.Pressure = weatherData.Main.Pressure
	weatherRecord.Humidity = weatherData.Main.Humidity
	weatherRecord.UpdatedAt = time.Now()

	return nil
}

func checkIfValidForecast(city string, db *gorm.DB) (models.Weather, bool, bool) {
	var weatherRecord models.Weather
	if err := db.Where("name = ?", city).First(&weatherRecord).Error; err != nil {
		if gorm.IsRecordNotFoundError(err) {
			return weatherRecord, false, false
		}
		log.Fatal("Error checking weather in database:", err)
	}

	// Ostatnia akutalizacja ponad godzine temu
	if time.Now().Unix()-weatherRecord.UpdatedAt.Unix() >= 3600 {
		return weatherRecord, false, true
	}
	log.Println("The weather is still valid, returning cached forecast.")
	return weatherRecord, true, true
}

func convertUnixToDate(unixTimestamp int64) time.Time {
	return time.Unix(unixTimestamp, 0)
}

func saveForecast(city string, weatherData WeatherResponse, db *gorm.DB) {
	weatherRecord := convertResponseToModel(city, weatherData)
	// Zapisz dane do bazy
	if err := db.Create(&weatherRecord).Error; err != nil {
		log.Fatal("Failed to insert data:", err)
	}
}
