package service;

import model.Request;
import sorting.InsertionSort;
import util.RequestFileUtil;

import java.io.IOException;
import java.util.*;

public class RequestService {

    // READ + SORT
    public List<Request> getSortedRequests()
            throws IOException {

        List<Request> requests =
                RequestFileUtil.readRequests();

        InsertionSort.sortByDate(requests);

        return requests;
    }

    // APPROVE / REJECT
    public void updateStatus(
            String requestId,
            String newStatus)
            throws IOException {

        List<Request> requests =
                RequestFileUtil.readRequests();

        for(Request r : requests){

            if(r.getRequestId()
                    .equals(requestId)){

                r.setStatus(newStatus);
            }
        }

        RequestFileUtil.writeRequests(requests);
    }

    // DELETE COMPLETED
    public void clearProcessedRequests()
            throws IOException {

        List<Request> requests =
                RequestFileUtil.readRequests();

        requests.removeIf(r ->
                r.getStatus()
                        .equalsIgnoreCase("APPROVED")
                        ||
                        r.getStatus()
                                .equalsIgnoreCase("REJECTED")
        );

        RequestFileUtil.writeRequests(requests);
    }
}
