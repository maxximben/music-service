package musicservice.tests;

import musicservice.account.AccountRepository;
import musicservice.jwt.JwtUtils;
import musicservice.s3.S3Service;
import musicservice.song.Song;
import musicservice.song.SongRepository;
import musicservice.track.Track;
import musicservice.track.TrackRepository;
import musicservice.user.User;
import musicservice.account.AccountService;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class TestController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private S3Service s3Service;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SongRepository songRepository;


    @GetMapping("findAll")
    public List<User> findAll() {
        return accountService.findAll();
    }

    @GetMapping("/getTrackById")
    public String getTrackById(@RequestParam int id) {
        return trackRepository.getTrackById(id);
    }

    @DeleteMapping("/deleteTrackById")
    public void deleteTrackById(@RequestParam int id) {
        trackRepository.deleteById(id);
    }

    @PostMapping("upload-audio")
    public void uploadAudio(@RequestParam("file")MultipartFile file, @RequestParam int userid) throws IOException, CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException {


        String url = s3Service.uploadAudio(file);

        File tempFile = File.createTempFile("audio", file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }

        // Read the audio file and extract metadata
        AudioFile audioFile = AudioFileIO.read(tempFile);
        Tag tag = audioFile.getTag();

        // Extract metadata (no further action needed as per request)
        String title = tag.getFirst(FieldKey.TITLE);
        String artist = tag.getFirst(FieldKey.ARTIST);
        String album = tag.getFirst(FieldKey.ALBUM);

        // Extract cover art if available
        byte[] cover = null;
        if (tag.getFirstArtwork() != null) {
            cover = tag.getFirstArtwork().getBinaryData();
        }

        String coverUrl = null;

        if (cover != null && cover.length > 0) {
            coverUrl = s3Service.uploadCover(cover, "cover-" + UUID.randomUUID() + ".jpg");
        }

        Track track = new Track(0, title, userid, album, coverUrl, url);

        tempFile.delete();

        trackRepository.addTrack(track);
    }

    @PostMapping("upload-image")
    public void uploadImage(@RequestParam("file")MultipartFile file) throws IOException {
        s3Service.uploadImage(file);
    }

    @GetMapping("/find-by-email")
    public User findByEmail(@RequestParam String email) {
        return accountRepository.findByEmail(email);
    }

    @PostMapping("/exist-by-email")
    public boolean exist(@RequestParam String email) {
        return accountRepository.existsByEmail(email);
    }

    @GetMapping("/get-song-by-id")
    public Song getSongById(@RequestParam int id) {
        return songRepository.getSongById(id);
    }

    @GetMapping("/get-author-by-song-id")
    public String getAuthorBySongId (@RequestParam int songId) {
        return songRepository.getAuthorBySongId(songId);
    }
}
