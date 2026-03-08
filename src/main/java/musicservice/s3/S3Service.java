package musicservice.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.public-url-prefix}")
    private String publicUrlPrefix;

    public String uploadAudio(MultipartFile file) throws IOException {

        String key = "audio/" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return publicUrlPrefix + "/" + key;
    }

    public void uploadImage(MultipartFile file) throws IOException {

        String key = "images/" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

    }


    public String uploadCover(byte[] cover, String fileName) {
        String key = "covers/" + fileName;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("image/jpeg")     // или определяй по расширению
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(cover));
        return publicUrlPrefix + "/" + key;
    }

    public String uploadByteArray(byte[] data, String key, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key("images/" + key)                           // например: "covers/album123.jpg"
                .contentType(contentType)           // важно! "image/jpeg", "image/png" и т.д.
                // .contentLength(data.length)      // можно указать явно, но не обязательно
                .build();

        RequestBody requestBody = RequestBody.fromBytes(data);

        PutObjectResponse response = s3Client.putObject(putObjectRequest, requestBody);

        return response.eTag();  // или можно вернуть ключ, URL и т.д.
    }

}
