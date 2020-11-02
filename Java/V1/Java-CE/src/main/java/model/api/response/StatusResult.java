package model.api.response;

public class StatusResult {
    Status status;

    int memory;

    int signal;

    String signal_desc;

    Streams streams;

    String uri;

    public Status getStatus() {
        return status;
    }

    public int getMemory() {
        return memory;
    }

    public int getSignal() {
        return signal;
    }

    public String getSignal_desc() {
        return signal_desc;
    }

    public Streams getStreams() {
        return streams;
    }

    public float getTime() {
        return time;
    }

    float time;

    public StatusResult(Status status, int memory, int signal, String signal_desc, Streams streams, float time) {
        this.status = status;
        this.memory = memory;
        this.signal = signal;
        this.signal_desc = signal_desc;
        this.streams = streams;
        this.time = time;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatusResult{");
        sb.append("status=").append(status);
        sb.append(", memory=").append(memory);
        sb.append(", signal=").append(signal);
        sb.append(", signal_desc='").append(signal_desc).append('\'');
        sb.append(", streams=").append(streams);
        sb.append(", time=").append(time);
        sb.append('}');
        return sb.toString();
    }
}
