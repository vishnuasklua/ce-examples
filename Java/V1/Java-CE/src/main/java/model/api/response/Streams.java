package model.api.response;

public class Streams {
    Stream source;

    Stream input;

    Stream output;

    Stream error;

    public Stream getSource() {
        return source;
    }

    public Stream getInput() {
        return input;
    }

    public Stream getOutput() {
        return output;
    }

    public Stream getError() {
        return error;
    }

    public Stream getCmpinfo() {
        return cmpinfo;
    }

    Stream cmpinfo;

    public Streams(Stream source, Stream input, Stream output, Stream error, Stream cmpinfo) {
        this.source = source;
        this.input = input;
        this.output = output;
        this.error = error;
        this.cmpinfo = cmpinfo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("model.api.response.Streams{");
        sb.append("source=").append(source);
        sb.append(", input=").append(input);
        sb.append(", output=").append(output);
        sb.append(", error=").append(error);
        sb.append(", cmpinfo=").append(cmpinfo);
        sb.append('}');
        return sb.toString();
    }
}
