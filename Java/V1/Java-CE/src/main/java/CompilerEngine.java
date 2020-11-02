import model.api.response.SubmissionStatusResponse;
import model.api.response.SubmissionStatusesResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CompilerEngine {

    public static void main(String[] args) throws InterruptedException {
        String baseUrl = "https://www.compiler-engine.com/api/v1/submissions";

        // Place your compiler apiToken. You can get token from https://www.compiler-engine.com/apiTokens
        String apiToken = "<API_TOKEN>";

        // Initialize the Compiler-Engine Client
        CompilerEngineClient compilerEngineClient = new CompilerEngineClient(baseUrl, apiToken);

        // compilerId 116 belong to python3. *Required Field*
        String compilerId = "116";

        // source is your source code for execution. *Required Field*
        String source = "print(input())";

        // Input for your source code
        String input = "Compiler-Engine-Output";

        // Maximum time limit in seconds. Default is 5s
        int timeLimit = 5;

        String compileId = compilerEngineClient
                .createSubmissionAndGetCompileId(compilerId, source, input, timeLimit);
        if (compileId == null || compileId.equals("")) {
            System.out.println("Something went wrong. Check console");
            return;
        }
        System.out.println("CompileId: " + compileId);

        System.out.print("\nChecking submission status");
        SubmissionStatusResponse submissionStatusResponse = null;
        boolean completed = false;
        for (int i = 0; i < 6; i++) {

            // Sleep/wait for 2 seconds before calling submission status
            TimeUnit.SECONDS.sleep(2);
            System.out.print("...");
            submissionStatusResponse = compilerEngineClient.getSubmissionStatus(compileId);
            if (submissionStatusResponse == null) {
                System.out.println("Something went wrong. CompileId - " + compileId);
                break;
            }

            // If the code is executed then get the stream next else retry after 2s
            if (submissionStatusResponse.getExecuting().equals("false")) {
                completed = true;
                break;
            }
        }

        if (!completed) {
            System.out.println("Too much time taken for execution. Something went wrong. CompileId - " + compileId);
            return;
        }

        System.out.println("\nProgram executed");
        String streamUrl = "";
        // Handle different status code. The below handling is just for example
        switch (submissionStatusResponse.getResult().getStatus().getCode()) {
            case 15:
                streamUrl = submissionStatusResponse.getResult().getStreams().getOutput().getUrl();
                break;
            case 11:
                streamUrl = submissionStatusResponse.getResult().getStreams().getCmpinfo().getUrl();
                break;
            case 17:
                streamUrl = submissionStatusResponse.getResult().getStreams().getError().getUrl();
                break;
            case 20:
            case 12:
            case 13:
                System.out.println("Code status - ");
                System.out.println(submissionStatusResponse.getResult().getStatus().getName());
                break;
            default:
                System.out.println("Wrong status code");
        }

        // Get the output stream
        String output = compilerEngineClient.getStreamOutput(streamUrl);
        if (output == null) {
            System.out.println("Something went wrong");
        } else {
            System.out.println("\nProgram output - ");
            System.out.println("-----------** OUTPUT START **--------------\n");
            System.out.println(output);
            System.out.println("-----------** OUTPUT END  **----------------");
        }
    }
}
