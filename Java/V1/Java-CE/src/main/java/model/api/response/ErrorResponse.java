package model.api.response;

import java.util.List;

public class ErrorResponse {
    String message;

    List<Object> data;

    int error_code;

    public ErrorResponse(String message, List<Object> data, int error_code) {
        this.message = message;
        this.data = data;
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return error_code;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("model.api.response.ErrorResponseObject{");
        sb.append("message='").append(message).append('\'');
        sb.append(", date=").append(data);
        sb.append(", error_code=").append(error_code);
        sb.append('}');
        return sb.toString();
    }
}
