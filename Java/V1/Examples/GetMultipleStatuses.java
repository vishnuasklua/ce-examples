import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GetMultipleStatuses {

    // Place your api-token here
    private static final String APITOKEN = "<API_TOKEN>";

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /**
     * return query params from ids
     * 
     * @param ids list of compile-id
     * @return String
     */
    static String getQueryParamsFromIds(List<String> ids) {
        StringBuilder stringBuilder = new StringBuilder("?ids=");
        for (String id : ids) {
            stringBuilder.append(id);
            stringBuilder.append(",");
        }

        // to remove extra ',' in the end
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();
    }
    public static void main(String[] args) throws IOException, InterruptedException {

        try {
            String baseUrl = "https://www.compiler-engine.com/api/v1/submissions";
            List<String> ids = new ArrayList<>();
            ids.add("<COMPILE_ID>");
            ids.add("<COMPILE_ID>");
            String queryParams = getQueryParamsFromIds(ids);
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(baseUrl + queryParams))
                    .setHeader("Authorization", "Bearer " + APITOKEN)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // print status code
            System.out.println(response.statusCode());

            // print response body
            System.out.println(response.body());
        } catch (ConnectException connectException) {
            System.out.println("Please check your internet connection");
        }
    }
}
