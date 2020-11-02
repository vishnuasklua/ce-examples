package model.api.response;

public class ApiResponse {
    int responseCode;
    String body;

    public ApiResponse(int responseCode, String body) {
        this.responseCode = responseCode;
        this.body = body;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getBody() {
        return body;
    }
}
