#!/bin/bash
echo "Esperando a que MySQL esté disponible..."

while ! nc -z db 3306; do
  echo "MySQL aún no está listo – esperando..."
  sleep 2
done

echo "MySQL está listo – iniciando backend"
exec java -jar app.jar
