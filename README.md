# Building a Simple Doctor's Appointment Application Using Java Spring

This repository contains the code for a simple doctor's appointment system using Java Spring to manage appointments between doctors and patients. The system allows patients to view and book appointments, while the doctor can manage available slots. Below are the key features, stories, and test cases covered in the project.

## Overview

The project centers around a single doctor who can create open time slots for appointments. Patients can view and book these appointments by entering their personal information. Several features and edge cases are handled, including validations, error handling, and concurrency checks.

### Technologies Used

- **Java 17**
- **Spring Boot 3**
- **Hibernate Envers** for auditing
- **PostgreSQL** as the database

## Features and User Stories

### 1. Doctor Adds Open Times

The doctor can add available times for appointments by specifying a start and end time. These times are broken down into 30-minute slots. If any slot is shorter than 30 minutes, it is ignored.

#### Test Cases:
- If the end time is earlier than the start time, an appropriate error message is shown.
- If the time difference between start and end is less than 30 minutes, no time slots are added.

### 2. Doctor Views 30-Minute Appointments

The doctor can view both open and booked appointments. For taken appointments, the system shows the patient’s phone number and name.

#### Test Cases:
- If there are no appointments, an empty list is displayed.
- If some appointments are taken, the patient’s phone number and name are displayed.

### 3. Doctor Deletes Open Appointments

The doctor can delete any open (unbooked) appointment. The system ensures that deletion cannot happen if a patient is booking the appointment at the same time.

#### Test Cases:
- If there are no open appointments, a `404 Not Found` error is returned.
- If the appointment is already taken, a `406 Not Acceptable` error is shown.
- Concurrency checks are performed to ensure that the appointment cannot be deleted while it is being booked.

### 4. Patients View Doctor's Open Appointments

Patients can view all available appointment slots for a given day. They can then choose an appointment to book.

#### Test Cases:
- If no open appointments are available for the day, an empty list is shown.

### 5. Patients Take Open Appointments

Patients can book an available appointment by entering their phone number and name. Several validations ensure the process works correctly.

#### Test Cases:
- If either the phone number or name is missing, an error message is shown.
- If the appointment is already taken or deleted, an appropriate error message is shown.
- Concurrency checks ensure that patients cannot book or delete the same appointment simultaneously.

### 6. Patients View Their Own Appointments

Patients can view their booked appointments by providing only their phone number. All appointments associated with that phone number are shown.

#### Test Cases:
- If no appointment is found with the provided phone number, an empty list is shown.
- If multiple appointments have been taken by the patient, all are listed.

## How to Run the Project

1. Clone the repository:

   ```bash
   git clone https://github.com/glovistts/DoctorAppoinment.git
