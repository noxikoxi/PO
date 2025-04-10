package main

import (
	"github.com/jinzhu/gorm"
	"github.com/joho/godotenv"
	"github.com/labstack/echo/v4"
	"log"
	"main/controllers"
	"main/models"
	"os"
)

// Pogoda z Api https://openweathermap.org/

func main() {
	e := echo.New()
	if err := godotenv.Load(); err != nil {
		log.Fatal("Error loading .env file")
	}

	db, err := gorm.Open("sqlite3", "weather.db")
	if err != nil {
		log.Fatal("Failed to connect to the database:", err)
	}
	defer db.Close()

	models.Migrate(db)

	token := os.Getenv("TOKEN")
	if token == "" {
		log.Fatal("TOKEN not set in .env file")
	}

	weatherController := controllers.NewWeatherController(token, db)

	e.GET("/:city", weatherController.GetWeatherForecast)
	e.Logger.Fatal(e.Start(":1323"))
}
