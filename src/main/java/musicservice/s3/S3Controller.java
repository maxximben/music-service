package musicservice.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

import java.util.ArrayList;
import java.util.List;

@RestController
public class S3Controller {

    @Autowired
    private S3Client s3Client;

    @GetMapping("/api/files")
    public ResponseEntity<List<FileInfo>> listFiles(
            @RequestParam String bucket,
            @RequestParam(required = false) String prefix,
            @RequestParam(required = false, defaultValue = "1000") int maxKeys
    ) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(prefix != null ? prefix : "")
                .maxKeys(maxKeys)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);

        List<FileInfo> files = new ArrayList<>();

        for (var obj : response.contents()) {
            String key = obj.key();
            files.add(new FileInfo(key, getFileName(key)));
        }

        return ResponseEntity.ok(files);
    }

    private String getFileName(String key) {
        int lastSlash = key.lastIndexOf('/');
        return lastSlash >= 0 ? key.substring(lastSlash + 1) : key;
    }
}

record FileInfo(
        String key,
        String fileName
) {}
