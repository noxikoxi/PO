package models

import (
	"github.com/jinzhu/gorm"
	"time"
)

type Weather struct {
	Name        string `gorm:"primary_key"`
	Sunrise     time.Time
	Sunset      time.Time
	Weather     string
	Description string
	Temp        float32
	FeelsLike   float32
	TempMin     float32
	TempMax     float32
	Pressure    int
	Humidity    int
	UpdatedAt   time.Time
}

// Migrate Funkcja inicjująca migrację modelu Weather
func Migrate(db *gorm.DB) {
	db.AutoMigrate(&Weather{})
}
