# StayNext

## Short Description

StayNext is a Spring Boot based smart PG marketplace and management platform where tenants can discover verified PG accommodations, while PG owners manage rooms, bookings, tenants, rent, and complaints from one platform. Admin users approve owners/listings and monitor the platform.

## Tagline

Find, Book & Manage PGs Smarter

## Tech Stack

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Neon Database
- Thymeleaf
- Bootstrap
- HTML, CSS, JavaScript

## Main Modules

- StayNext Admin Portal
- StayNext Owner Portal
- StayNext Tenant Portal
- Public Marketing Website

## Key Features

- Role-based login
- Public landing pages for Home, About, Features, and Contact
- Tenant-only PG search by city/area
- PG owner registration with admin approval
- Verified PG listing approval system
- Smart room search
- Easy booking request workflow
- Tenant assignment
- Tenant management
- Rent tracking
- Complaint resolution
- Cloudinary image upload for PG listings

## Setup Instructions

### 1. Clone Project

```bash
git clone <repository-url>
cd PgManagement
```

### 2. Configure Neon PostgreSQL

Create a `.env` file in the project root. A real local `.env` is already available for development and is ignored by Git.

```properties
DATABASE_URL=jdbc:postgresql://your-neon-host/neondb?sslmode=require
DATABASE_USERNAME=your_database_username
DATABASE_PASSWORD=your_database_password
PORT=8081
JPA_DDL_AUTO=update
JPA_SHOW_SQL=false
HIBERNATE_FORMAT_SQL=false
THYMELEAF_CACHE=true
ADMIN_BOOTSTRAP_ENABLED=true
ADMIN_EMAIL=admin@example.com
ADMIN_PASSWORD=change_this_password
DATA_SEEDER_ENABLED=false
CLOUDINARY_CLOUD_NAME=your_cloudinary_cloud_name
CLOUDINARY_API_KEY=your_cloudinary_api_key
CLOUDINARY_API_SECRET=your_cloudinary_api_secret
```

### 3. Application Properties

Open:

```text
src/main/resources/application.properties
```

The application reads database and production settings from environment variables.

For deployment, keep `DATA_SEEDER_ENABLED=false` so demo PGs, tenants, bookings, and rent records are not inserted into the live database. Keep `ADMIN_BOOTSTRAP_ENABLED=true` on the first deployment so the admin account is created automatically.

### 4. Run Project

On Windows:

```powershell
$env:DATABASE_URL="jdbc:postgresql://your-neon-host/neondb?sslmode=require"
$env:DATABASE_USERNAME="your_database_username"
$env:DATABASE_PASSWORD="your_database_password"
.\mvnw.cmd spring-boot:run
```

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

Open:

```text
http://localhost:8081
```

## Docker Deployment

Build the Docker image:

```bash
docker build -t staynext .
```

Run with the `.env` file:

```bash
docker run --env-file .env -p 8081:8081 -v staynext_uploads:/app/uploads staynext
```

Or use Docker Compose:

```bash
docker compose up --build
```

For cloud deployment, set these environment variables in your hosting platform:

```text
DATABASE_URL
DATABASE_USERNAME
DATABASE_PASSWORD
PORT
JPA_DDL_AUTO
JPA_SHOW_SQL
HIBERNATE_FORMAT_SQL
THYMELEAF_CACHE
ADMIN_BOOTSTRAP_ENABLED
ADMIN_EMAIL
ADMIN_PASSWORD
DATA_SEEDER_ENABLED
CLOUDINARY_CLOUD_NAME
CLOUDINARY_API_KEY
CLOUDINARY_API_SECRET
```

## Login Credentials

The admin account is bootstrapped from `ADMIN_EMAIL` and `ADMIN_PASSWORD` if that email does not already exist.

| Role | Email | Password |
| --- | --- | --- |
| Admin | value of `ADMIN_EMAIL` | value of `ADMIN_PASSWORD` |

Demo owner, tenant, PG, booking, complaint, and rent records are inserted only when `DATA_SEEDER_ENABLED=true` and the users table is empty.

## Deployment Checklist

- Set all database, Cloudinary, and admin environment variables on the hosting platform.
- Use a strong `ADMIN_PASSWORD` before the first deployment.
- Keep `.env` private and never commit it.
- Keep `DATA_SEEDER_ENABLED=false` for production.
- Use `JPA_DDL_AUTO=update` for simple demos; use migrations before serious production use.
- Run `./mvnw test` or `.\mvnw.cmd test` before deploying.

## Project Workflow Diagram

```text
Tenant
  |
  |-- Login to StayNext Tenant Portal
  |-- Search PGs by city/area, PG type, and rent
  |-- View approved PG details, rooms, facilities, rent, and images
  |-- Send booking/visit request
  |
PG Owner
  |
  |-- Register with PG name and address
  |-- Wait for admin approval before login
  |-- Login to StayNext Owner Portal
  |-- Add PG listing and upload image
  |-- Add rooms and manage bed availability
  |-- Accept/reject booking request
  |-- Accepted booking creates tenant assignment and pending rent
  |-- Manage rent payments and complaints
  |
Admin
  |
  |-- Login to StayNext Admin Portal
  |-- Approve/reject pending PG owner registrations
  |-- Manage users
  |-- Approve/reject PG listings
  |-- Monitor bookings, complaints, and rent payments
  |
Public Website
  |
  |-- Marketing pages only: Home, About, Features, Contact, Login, Register
```

## Project Structure

```text
src/main/java/com/major/pgmanagement
|-- config
|-- controller
|-- dto
|-- entity
|-- exception
|-- repository
`-- service

src/main/resources
|-- static
|   |-- css
|   |-- js
|   `-- images
`-- templates
    |-- admin
    |-- auth
    |-- error
    |-- fragments
    |-- owner
    |-- public
    `-- tenant
```

## Uploaded Images

PG images are uploaded to Cloudinary in:

```text
staynext/pg-images
```

The app stores the returned Cloudinary secure image URL in the `PgListing.imageUrl` field.

Older local image URLs can still be served through:

```text
/uploads/**
```
