package musicservice.search;

public record SearchItem (
        String cover,
        String name,
        String type,
        String author,
        int id
) {}