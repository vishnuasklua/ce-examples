import com.google.gson.Gson;
import model.api.response.*;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CompilerEngineClient {

    private final String baseUrl, apiToken, userAgent;

    /**
     * Constructor
     *
     * @param apiToken - Compiler access token found in https://www.compiler-engine.com/apiTokens
     * @param baseUrl  - Compiler execution endpoint
     */
    CompilerEngineClient(String baseUrl, String apiToken) {
        this.baseUrl = baseUrl;
        this.apiToken = apiToken;
        userAgent = "CompilerEngine/JAVA-CE";
    }

    /**
     *  To make the API call
     *
     * @param url - Api url
     * @param method - Method to call. Accepted values for this example are - GET, POST.
     * @param requestBody - POST body
     * @return ApiResponse - this contains the response code and response body
     */
    ApiResponse apiCall(@NotNull String url, @NotNull String method, RequestBody requestBody) {
        OkHttpClient client = new OkHttpClient();

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder
                .url(url)
                .header("User-Agent", userAgent)
                .header("Authorization", "Bearer " + apiToken);

        if (method.equals("POST")) {
            requestBuilder.post(requestBody);
        } else if (method.equals("GET")) {
            requestBuilder.get();
        }

        Request request = requestBuilder.build();
        String content = "";
        ApiResponse apiResponse;
        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();

            if (body != null) {
                content = body.string();
            }

            apiResponse = new ApiResponse(response.code(), content);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            apiResponse = new ApiResponse(-1, "Something went wrong");
        }

        return apiResponse;
    }

    /**
     * Create a new submission
     *
     * @param compilerId Compiler ID of the language
     * @param source     source code of the program
     * @param input      data that will be given to the program on stdin
     * @param timeLimit  Execution time of the program
     * @return CompileID
     */
    public String createSubmissionAndGetCompileId(@NotNull String compilerId, @NotNull String source, String input,
                                                  int timeLimit) {
        // Set POST body
        FormBody.Builder form = new FormBody.Builder();
        if (compilerId.equals("") || source.equals("") || timeLimit < 0) {
            System.out.println("Invalid arguments");
            return null;
        }

        form.add("compilerId", compilerId);
        form.add("source", source);
        if (input != null && !input.equals("")) {
            form.add("input", input);
        }

        if (timeLimit >= 1) {
            form.add("timeLimit", "" + timeLimit);
        }
        RequestBody formBody = form.build();

        CreateSubmissionResponse createSubmissionResponse = null;

        // Make the api call
        ApiResponse apiResponse = this.apiCall(baseUrl, "POST", formBody);
        if (apiResponse.getResponseCode() == -1 || apiResponse.getResponseCode() == 0) {
            System.out.println("Something went wrong");
        }

        Gson gson = new Gson();

        // if API is successful the convert the response body to java object
        if (apiResponse.getResponseCode() == 200) {
            createSubmissionResponse = gson.fromJson(apiResponse.getBody(), CreateSubmissionResponse.class);
        }

        if (apiResponse.getResponseCode() >= 400 && apiResponse.getResponseCode() < 600) {
            ErrorResponse errorResponse;
            errorResponse = gson.fromJson(apiResponse.getBody(), ErrorResponse.class);
            System.out.println("Error received as response - ");
            System.out.println(errorResponse.getMessage());
        }

        if (createSubmissionResponse != null) {
            return createSubmissionResponse.getId();
        }

        return "";
    }

    /**
     * Get the status of a code submission
     *
     * @param compileId Code execution id received from creating a submission
     * @return SubmissionStatusResponse - Submission status response in java object format
     */
    public SubmissionStatusResponse getSubmissionStatus(String compileId) {
        String statusUrl = baseUrl + "/" + compileId;

        SubmissionStatusResponse submissionStatusResponse = null;
        ApiResponse apiResponse = this.apiCall(statusUrl, "GET", null);
        if (apiResponse.getResponseCode() == -1 || apiResponse.getResponseCode() == 0) {
            System.out.println("Something went wrong");
        }
        Gson gson = new Gson();

        // if API is successful the convert the response body to java object
        if (apiResponse.getResponseCode() == 200) {
            submissionStatusResponse = gson.fromJson(apiResponse.getBody(), SubmissionStatusResponse.class);
        }

        if (apiResponse.getResponseCode() >= 400 && apiResponse.getResponseCode() < 600) {
            ErrorResponse errorResponse;
            errorResponse = gson.fromJson(apiResponse.getBody(), ErrorResponse.class);
            System.out.println("Error received as response - ");
            System.out.println(errorResponse.getMessage());
        }

        return submissionStatusResponse;
    }

    /**
     * Get the raw stream
     *
     * @param streamUrl Url of which stream of data(Eg. output, error, source, etc) that needs to be fetched
     * @return Stream data(file content) as a string
     */
    public String getStreamOutput(String streamUrl) {
        String output = null;
        if (streamUrl != null && !streamUrl.equals("")) {
            ApiResponse apiResponse = apiCall(streamUrl, "GET", null);
            if (apiResponse.getResponseCode() == -1 || apiResponse.getResponseCode() == 0) {
                System.out.println("Something went wrong");
            }

            Gson gson = new Gson();
            if (apiResponse.getResponseCode() == 200) {
                output = apiResponse.getBody();
            }

            if (apiResponse.getResponseCode() >= 400 && apiResponse.getResponseCode() < 600) {
                ErrorResponse errorResponse;
                errorResponse = gson.fromJson(apiResponse.getBody(), ErrorResponse.class);
                System.out.println("Error received as response - ");
                System.out.println(errorResponse.getMessage());
            }
        }
        return output;
    }

    /**
     * Get the execution status of list of compile-ids
     * @param ids List of compile ids
     * @return SubmissionStatusesResponse list of submission statuses
     */
    public SubmissionStatusesResponse getSubmissionStatuses(@NotNull List<String> ids) {
        if (ids.isEmpty()) {
            System.out.println("Ids should not be empty");
            return null;
        }

        StringBuilder idsQueryParam = new StringBuilder();
        for (String id: ids) {
            if (id.isBlank() || id.isEmpty()) {
                continue;
            }
            idsQueryParam.append(id).append(",");
        }

        // removing the extra ',' in the end
        String idsQuery = "";
        if(idsQueryParam.length() > 0) {
            idsQuery = idsQueryParam.substring(0, idsQueryParam.length() - 1);
        }

        // add the ids to query url
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(baseUrl)).newBuilder();
        urlBuilder.addQueryParameter("ids", idsQuery);
        String url = urlBuilder.build().toString();

        SubmissionStatusesResponse submissionStatusesResponse = null;
        ApiResponse apiResponse = this.apiCall(url, "GET", null);
        if (apiResponse.getResponseCode() == -1 || apiResponse.getResponseCode() == 0) {
            System.out.println("Something went wrong");
        }
        Gson gson = new Gson();

        if (apiResponse.getResponseCode() == 200) {
            submissionStatusesResponse = gson.fromJson(apiResponse.getBody(), SubmissionStatusesResponse.class);
        }

        if (apiResponse.getResponseCode() >= 400 && apiResponse.getResponseCode() < 600) {
            ErrorResponse errorResponse;
            errorResponse = gson.fromJson(apiResponse.getBody(), ErrorResponse.class);
            System.out.println("Error received as response: ");
            System.out.println(apiResponse);
        }

        return submissionStatusesResponse;
    }
}

