#!/usr/bin/env bash
# Runs the whole system: Spring Boot backend on :8080, frontend on :3000.
# Requires: Java 17+, Maven, and Node (for `npx serve`) or Python 3.
set -e

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_DIR="$ROOT_DIR/../library-management-backend-v2/backend"

echo "Starting backend (Spring Boot, port 8080)..."
(cd "$BACKEND_DIR" && mvn -q spring-boot:run) &
BACKEND_PID=$!

trap "echo 'Stopping backend...'; kill $BACKEND_PID 2>/dev/null" EXIT

echo "Waiting for backend to come up..."
for i in $(seq 1 30); do
  if curl -s -o /dev/null http://localhost:8080/api/user/books; then
    echo "Backend is up."
    break
  fi
  sleep 1
done

echo "Starting frontend (port 3000)..."
if command -v npx >/dev/null 2>&1; then
  npx --yes serve -l 3000 "$ROOT_DIR"
elif command -v python3 >/dev/null 2>&1; then
  (cd "$ROOT_DIR" && python3 -m http.server 3000)
else
  echo "Need either Node (npx) or Python 3 installed to serve the frontend." >&2
  exit 1
fi
