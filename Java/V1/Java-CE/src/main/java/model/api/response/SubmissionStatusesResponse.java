package model.api.response;

import java.util.List;

public class SubmissionStatusesResponse {
    List<SubmissionStatusResponse> items;

    public List<SubmissionStatusResponse> getItems() {
        return items;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubmissionStatusListResponse{");
        sb.append("items=").append(items);
        sb.append('}');
        return sb.toString();
    }

}
