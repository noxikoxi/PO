#!/bin/sh

if [ ! -f ./db.sqlite ]; then
  echo "Pierwsze uruchomienie, wykonuję migracje"
  ./Run migrate --yes
else
  echo "Baza już istnieje, pomijam migracje"
fi
./Run serve --env production --hostname 0.0.0.0 --port 8080
