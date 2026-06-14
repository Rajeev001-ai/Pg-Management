# StayNext Project Summary

## Problem Statement

Finding a suitable PG accommodation is often difficult because tenants need to compare location, rent, rooms, facilities, availability, and owner contact details across many scattered sources. PG owners also need a simple way to manage listings, rooms, bookings, tenants, rent records, and complaints. Admins need control over owner approval, listing approval, and platform monitoring.

## Solution

StayNext provides a centralized smart PG marketplace and management platform for tenants, PG owners, and admins. Public visitors only see marketing pages and authentication links. Tenants can search and view approved PG listings after login, send booking requests, raise complaints after assignment, and view rent records. PG owners can manage listings, rooms, booking requests, rent payments, and complaints after admin approval. Admin users can approve owners, approve PG listings, manage users, and monitor the platform.

## User Roles

- Admin: Approves/rejects pending owners, manages users, approves/rejects PG listings, and monitors bookings, complaints, and rent payments.
- PG Owner: Registers with PG details, waits for admin approval, adds PG listings, uploads PG images, manages rooms, handles booking requests, manages rent, and resolves complaints.
- Tenant: Searches PGs, sends booking requests, views rent history, and raises complaints after active assignment.
- Public Visitor: Views only Home, About, Features, Contact, Login, and Register pages.

## Complete Workflow

```text
1. Public visitor opens the StayNext marketing website.
2. Tenant registers and can log in immediately.
3. PG owner registers with PG name and address.
4. Admin approves or rejects the pending owner registration.
5. Approved PG owner logs in and lists PG details, rent, facilities, rules, and image.
6. Admin reviews and approves/rejects the PG listing.
7. Tenant logs in and searches approved PGs by city/area, PG type, and rent range.
8. Tenant opens PG detail page and sends a booking/visit request.
9. PG owner accepts or rejects the request.
10. On acceptance:
   - Booking status becomes ACCEPTED.
   - Room available beds decrease.
   - Tenant assignment is created.
   - Pending rent payment is generated.
11. Tenant views rent history and can raise complaints after assignment.
12. PG owner manages rent payments and complaint status.
13. Admin monitors users, listings, bookings, complaints, and rent payments.
```

## Database Entities

- User
- PgListing
- Room
- BookingRequest
- TenantAssignment
- RentPayment
- Complaint

## Future Enhancements

- Google Maps API
- Online payment gateway
- Email/SMS notification
- Cloudinary image upload
- Review and rating system
- Mobile app
