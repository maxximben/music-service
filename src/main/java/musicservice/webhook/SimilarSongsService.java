package musicservice.webhook;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimilarSongsService {

    private final SongAnalysisRepository songAnalysisRepository;

    public SimilarSongsService(SongAnalysisRepository songAnalysisRepository) {
        this.songAnalysisRepository = songAnalysisRepository;
    }

    public List<Integer> getTop20SimilarSongIds(int songId) {
        return songAnalysisRepository.findTop20SimilarSongIdsByDiscogs(songId);
    }

    public List<String> getTop20SimilarSongTitles(int songId) {
        return songAnalysisRepository.findTop20SimilarSongTitlesByDiscogs(songId);
    }
}