package musicservice.playlist;

import musicservice.song.Song;

import java.util.List;

public record Playlist (
        int playlistId,
        String title,
        String cover,
        int countOfSongs,
        List<Song> songs
) {}
