import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CEHttpClient {

    // Place your api-token here
    private static final String APITOKEN = "<API_TOKEN>";

    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10)).build();

    /**
     * This function is used to get the POST data
     * 
     * @param data POST data
     * @return BodyPublisher
     */
    public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    /**
     * Read file and return the content as a string
     * 
     * @param fileName location of the file
     * @return String - file content
     * @throws IOException
     */
    public static String readFileAsString(String fileName) throws IOException {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    public static void main(String[] args) throws Exception {
        String baseUrl = "https://www.compiler-engine.com/api/v1/submissions";

        int timeLimit = 5;

        // compilerId 116 belong to python3
        String compilerId = "116";

        // you can read source/input from file using readFileAsString function above
        String source = "print(input())";
        String input = "compiler-engine";

        Map<Object, Object> postData = new HashMap<>();
        postData.put("source", source);
        postData.put("input", input);
        postData.put("compilerId", compilerId);
        postData.put("timeLimit", timeLimit);

        try {
            HttpRequest request = HttpRequest.newBuilder().POST(ofFormData(postData)).uri(URI.create(baseUrl))
                    .setHeader("Authorization", "Bearer " + APITOKEN)
                    .header("Content-Type", "application/x-www-form-urlencoded").build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // print status code
            System.out.println(response.statusCode());

            // print response body -
            System.out.println(response.body());
        } catch (ConnectException connectException) {
            System.out.println("Please check your internet connection");
        }

    }
}