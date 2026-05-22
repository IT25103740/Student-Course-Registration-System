# Member 4: Arachchi V.A.K.S.V
**Role**: Moderator - Request Auditing

## 🛠️ Backend Contributions
- **SortingUtils.insertionSortRequests()**: Explicit implementation of the **Insertion Sort Algorithm** for chronological ordering.
- **generateBatchList()**: Logic to isolate `PENDING` transactions for efficient staff review.
- **approveRequest() / rejectRequest()**: Atomic decision-making logic with side-effects on module capacity.
- **clearProcessedRequests()**: Bulk deletion logic for maintaining a clean audit trail.

## 🎨 Frontend Contributions
- **Template**: `moderator-auditing.html`
- **Features**:
  - **Floating Action Bar**: Contextual bar appearing only on row selection for **Batch Processing**.
  - **Zebra-Striped Audit Table**: High-density data view with light gray `#F8FAFC` headers.
  - **Algorithmic Badge**: Visual proof of the "Insertion Sort" logic being active in the backend.
  - **Status Borders**: Dynamic color-coded strips for PENDING, APPROVED, and REJECTED states.
