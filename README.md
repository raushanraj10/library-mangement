# Library Management System - Backend (v2: approval workflow)

Spring Boot (Java), everything stored in-memory using `ArrayList` (no database).

## What's new in this version
- Books now have **subjects** and **total/available copy counts** (not just yes/no availability).
- Borrowing and returning are **request + admin-approval** flows, not instant actions.
- Every borrow has a **due date** (14 days after approval) and, if returned late, a **fine**.
- A book still out past its due date is flagged **overdue** with a live estimated fine.
- Admin-only **book stats** (total titles/copies/available/borrowed) - not exposed to users.
- Subject **filter** and full-text **search** for browsing books.

## Roles
- **ADMIN** - manage users, manage books, see book count stats, approve/reject borrow & return requests.
- **USER** - browse/search/filter books, request to borrow, request to return, view "My Borrowed Books".

## Run it
```
cd backend
mvn spring-boot:run
```
Runs on http://localhost:8080

## Default login accounts
| username | password | role  |
|----------|----------|-------|
| admin    | admin123 | ADMIN |
| john     | john123  | USER  |

## The borrow/return lifecycle
```
User requests borrow  -->  PENDING_BORROW  -->  Admin approves  -->  BORROWED (due date set)
                                              -->  Admin rejects  -->  REJECTED

User requests return  -->  PENDING_RETURN  -->  Admin approves  -->  RETURNED (fine calculated if late)
                                              -->  Admin rejects  -->  back to BORROWED
```
While a book is `BORROWED` and past its due date, it's automatically flagged `overdue: true`
with a live `estimatedFine` - this is where a frontend would show the warning message.

## API endpoints

### Auth
| Method | URL             | Body                        |
|--------|-----------------|------------------------------|
| POST   | /api/auth/login  | { "username", "password" }  |

### Admin
| Method | URL                                 | Description                          |
|--------|--------------------------------------|----------------------------------------|
| GET    | /api/admin/users                      | list users                              |
| POST   | /api/admin/users                      | add user                                 |
| DELETE | /api/admin/users/{id}                 | delete user                               |
| GET    | /api/admin/books                       | list books (with total & available copies) |
| POST   | /api/admin/books                       | add book { title, author, subject, totalCopies } |
| PUT    | /api/admin/books/{id}                  | edit book                                   |
| DELETE | /api/admin/books/{id}                  | delete book                                  |
| GET    | /api/admin/books/stats                 | totalTitles, totalCopies, totalAvailable, totalBorrowed (admin only) |
| GET    | /api/admin/requests                    | all PENDING_BORROW / PENDING_RETURN requests |
| GET    | /api/admin/records                     | full borrow history (with overdue/fine info)  |
| PUT    | /api/admin/requests/{id}/approve-borrow | approve a borrow request                       |
| PUT    | /api/admin/requests/{id}/reject-borrow  | reject a borrow request                         |
| PUT    | /api/admin/requests/{id}/approve-return | approve a return (calculates fine if late)       |
| PUT    | /api/admin/requests/{id}/reject-return  | reject a return (book stays borrowed)             |

### User
| Method | URL                                      | Description                       |
|--------|--------------------------------------------|--------------------------------------|
| GET    | /api/user/books                              | list all books                        |
| GET    | /api/user/books/search?keyword=java          | search by title/author                 |
| GET    | /api/user/books/filter?subject=Programming    | filter by subject                       |
| GET    | /api/user/books/subjects                      | list of distinct subjects (for a dropdown) |
| POST   | /api/user/borrow-request/{bookId}/{userId}    | request to borrow a book                    |
| POST   | /api/user/return-request/{recordId}           | request to return a borrowed book             |
| GET    | /api/user/my-books/{userId}                    | this user's borrow history ("My Borrowed Books" page) |

## Notes on the "no. of books" requirement
`totalCopies` (how many copies the library owns) is only surfaced through the **admin**
endpoints (`/api/admin/books`, `/api/admin/books/stats`). The **user** endpoints
(`/api/user/books`, etc.) return the same `Book` object but the frontend should only
display `availableCopies` to users, not `totalCopies` - the field is still technically
present in the JSON for simplicity (no separate DTOs), it's just not meant to be shown
on the user-facing pages.

## Next step
Frontend (React) will be added once you ask for it: a Login page, an Admin dashboard
(manage users/books, book count stats, approve/reject requests table), and a User side
(browse/search/filter books, "My Borrowed Books" page with due dates, overdue warnings,
and a confirm step before sending a return request).
