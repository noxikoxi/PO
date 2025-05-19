#!/bin/sh
./zadanie07 migrate --yes
./zadanie07 serve --env production --hostname 0.0.0.0 --port 8080
