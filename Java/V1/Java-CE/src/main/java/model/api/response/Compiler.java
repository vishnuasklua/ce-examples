package model.api.response;

public class Compiler {
    String id;

    String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Compiler(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("model.api.response.Compiler{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

