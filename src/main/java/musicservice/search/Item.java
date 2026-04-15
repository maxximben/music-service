package musicservice.search;

public record Item(
        String cover,
        String name,
        String type,
        String author,
        int id
) {}