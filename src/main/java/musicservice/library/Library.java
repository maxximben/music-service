package musicservice.library;

import musicservice.search.Item;

import java.util.List;

public record Library (
        List<Item> items
) {}
