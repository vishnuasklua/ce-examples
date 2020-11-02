package model.api.response;

public class Stream {
    int size;

    String url;

    public int getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public Stream(int size, String url) {
        this.size = size;
        this.url = url;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Stream{");
        sb.append("size=").append(size);
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
