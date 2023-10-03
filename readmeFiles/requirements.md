# Software Requirements

## Vision:

The vision of our product, Powerup Gym, is to provide a comprehensive management system for gyms and fitness centers. It aims to streamline administrative tasks, enhance the user experience for both trainers and players and facilitate efficient membership management. This product solves the pain points of manual administrative work in gyms, lack of access to vital information for trainers and players, and inefficient membership tracking.

---

## Scope (In/Out):

**IN - What the Product Will Do**

1. SuperAdmin can register players and trainers,  manage gym packages and classes.
2. Players can view and edit their profiles, change passwords, view membership details, and sign up for classes/packages.
3. Trainers can view and edit their profiles, change passwords, and view assigned classes.

**OUT - What the Product Will Not Do**

1. The product will not provide features for trainers or players to register themselves without SuperAdmin/Admin approval.

---

## Minimum Viable Product (MVP):

- SuperAdmin registration and the ability to add trainers and players.
- Secure login/logout system.
- SuperAdmin's capability to manage gym packages and classes.
- Players can view and edit profiles, change passwords, view membership details, and sign up for classes/packages.
- Trainers can view and edit profiles, change passwords, and view assigned classes.

---

## Stretch Goals:

1. Integration with payment processing for membership fees.
2. Implementing a notification system for class reminders and updates.
3. Advanced reporting and analytics for gym performance tracking.

---

## Functional Requirements:

1. SuperAdmin can create and delete user accounts.
2. Admin can manage gym packages and classes.
3. Players can update their profile information, change passwords, view memberships, and sign up for classes/packages.
   4.Trainers can update their profile information, change passwords, and view assigned classes.

---

## Data Flow:

- SuperAdmin logs in and adds trainers and players.
- SuperAdmin logs in and manages gym packages and classes.
- Players log in, view/edit profiles, and sign up for classes/packages.
- Trainers log in, view/edit profiles, and view assigned classes.

---

## Non-Functional Requirements:

1. **Security:** The system will implement role-based access control to ensure that only authorized users can perform specific actions. Data will be securely stored and transmitted using encryption.

2. **Usability:** The user interface will be intuitive and user-friendly, with clear navigation and responsive design to enhance the user experience. Additionally, the system will be tested for accessibility to accommodate users with disabilities.