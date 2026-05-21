# Member 1: Umer D.R.S
**Role**: Student - Enrollment Management

## 🛠️ Backend Contributions
- **submitEnrollmentRequest()**: Logic to check module capacity and assign `PENDING` or `WAITLISTED` status.
- **swapModule()**: Implementation of the atomic module exchange logic for pending requests.
- **withdrawRequest()**: Secure deletion of pending enrollment records.
- **getAvailableModules()**: Data provider for the module discovery marketplace.

## 🎨 Frontend Contributions
- **Template**: `student-enrollment.html`
- **Features**:
  - **Marketplace Cards**: Replaced standard tables with a modern "Shoppable" grid.
  - **Real-Time Capacity Bars**: Visual track-and-thumb progress bars.
  - **Side-Panel Marketplace**: Smooth sliding interface for course swapping.
  - **Dynamic Buttons**: Intelligent labels that switch between "Enroll" and "Waitlist".
