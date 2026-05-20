package model;

public class Request {

    private String requestId;
    private String studentId;
    private String requestType;
    private String status;
    private String date;

    public Request(String requestId,
                   String studentId,
                   String requestType,
                   String status,
                   String date) {

        this.requestId = requestId;
        this.studentId = studentId;
        this.requestType = requestType;
        this.status = status;
        this.date = date;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
