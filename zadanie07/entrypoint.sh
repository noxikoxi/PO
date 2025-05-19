#!/bin/sh

if [ ! -f ./db.sqlite ]; then
  echo "Pierwsze uruchomienie, wykonuję migracje"
  ./zadanie07 migrate --yes
else
  echo "Baza już istnieje, pomijam migracje"
fi
./zadanie07 serve --env production --hostname 0.0.0.0 --port 8080
