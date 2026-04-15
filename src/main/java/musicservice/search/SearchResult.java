package musicservice.search;

import java.util.List;

public record SearchResult (
        List<Item> items
) {}