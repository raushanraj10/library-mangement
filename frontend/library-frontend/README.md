# Library Manager — Frontend

A single-file React frontend (no build step) for the Spring Boot backend in
`library-management-backend-v2`. Covers both roles from the backend:

- **USER**: browse/search/filter books, request to borrow, view "My books"
  with due dates, overdue flags and fines, request returns.
- **ADMIN**: dashboard stats, manage books (add/edit/delete), manage users
  (add/delete), approve/reject pending borrow & return requests, view full
  borrow history with fines.

## Important — CORS and the port

The backend only allows requests from `http://localhost:3000`
(`@CrossOrigin(origins = "http://localhost:3000")` on every controller). So
this file must be served from port 3000 — opening it directly as a
`file://` page or serving it from another port will fail with a CORS error.

1. Start the backend first (default port 8080):
   ```
   cd library-management-backend-v2/backend
   mvn spring-boot:run
   ```
2. Serve this folder on port 3000, e.g. with Node's `serve`:
   ```
   npx serve -l 3000 .
   ```
   or Python:
   ```
   python3 -m http.server 3000
   ```
3. Open `http://localhost:3000` in your browser.

Or run both at once with the included `start.sh` (needs Maven + Node or
Python on your PATH): place this `library-frontend` folder next to
`library-management-backend-v2` and run `./start.sh`.

If your backend runs on a different host/port, change `API_BASE` near the
top of the `<script type="text/babel">` block in `index.html`.

## Seed accounts

- Admin: `admin` / `admin123`
- User: `john` / `john123`

## About "data not removed after logout"

Login **session** is cleared on logout (as expected for security), but
everything the app has fetched — books, requests, borrow records, stats —
is cached in `localStorage` under the key `library_data_cache` and is
**not** wiped on logout. So the last-seen data reappears instantly next
time someone signs in, and is refreshed from the server in the background.
Clear it manually with your browser's dev tools if you ever want a clean
slate (`localStorage.removeItem('library_data_cache')`).

## Notes

- Plain HTML + React (via CDN, in-browser Babel) — no npm install needed.
- All styling is in the same file; no external frontend framework/build tool.
