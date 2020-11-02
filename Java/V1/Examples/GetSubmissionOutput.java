import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class GetSubmissionOutput {

    // Place your api-token here
    private static final String APITOKEN = "<API_TOKEN>";

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {

        try {
            String baseUrl = "https://www.compiler-engine.com/api/v1/submissions";
            
            String compileId = "<COMPILE_ID>";

            // Place which stream you want. Accepted values are - source|input|output|cmpinfo|error
            String stream = "output";
            
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(baseUrl + "/" + compileId + "/" + stream))
                    .setHeader("Authorization", "Bearer " + APITOKEN)
                    .build();

            // Make the API call
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // print status code
            System.out.println(response.statusCode());

            // print response body
            System.out.println(response.body());
        } catch(ConnectException connectException) {
            System.out.println("Please check your internet connection");
        }
    }
}
