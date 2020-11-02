package model.api.response;

public class SubmissionStatusResponse {
    String id;

    String date;

    String executing;

    Compiler compiler;

    StatusResult result;

    String uri;

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getExecuting() {
        return executing;
    }

    public Compiler getCompiler() {
        return compiler;
    }

    public StatusResult getResult() {
        return result;
    }

    public String getUri() {
        return uri;
    }

    public SubmissionStatusResponse(String id, String date, String executing, Compiler compiler,
                                    StatusResult result, String uri) {
        this.id = id;
        this.date = date;
        this.executing = executing;
        this.compiler = compiler;
        this.result = result;
        this.uri = uri;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubmissionStatusResponse{");
        sb.append("id='").append(id).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append(", executing='").append(executing).append('\'');
        sb.append(", compiler=").append(compiler);
        sb.append(", result=").append(result);
        sb.append(", uri='").append(uri).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
