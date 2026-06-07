#!/bin/bash

# Запуск Nginx
service nginx start

# Запуск Spring Boot (исправлен хост postgres, а не localhost)
java -jar app.jar \
  --spring.datasource.url=jdbc:postgresql://postgres:5432/sb5_db \
  --spring.datasource.username=sb5_user \
  --spring.datasource.password=sb5_password \
  --server.port=8088