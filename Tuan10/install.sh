#!/bin/bash

services=(
  "food-service"
  "frontend"
  "order-service"
  "payment-service"
  "user-service"
)

for service in "${services[@]}"
do
  echo "==============================="
  echo "Starting $service..."
  echo "==============================="

  cd "$service" || exit

  # Run dev server in background
  npm install &

  cd ..
done

# Wait all background processes
wait