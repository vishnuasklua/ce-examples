package model.api.response;

public class CreateSubmissionResponse {
    String id;

    public CreateSubmissionResponse() {
        id = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CreateSubmissionResponse(String id) {
        this.id = id;
    }
}
