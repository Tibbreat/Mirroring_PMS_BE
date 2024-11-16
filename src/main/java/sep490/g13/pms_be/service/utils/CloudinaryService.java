package sep490.g13.pms_be.service.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sep490.g13.pms_be.exception.other.BadCredentialsException;
import sep490.g13.pms_be.model.response.CloudinaryResponse;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    @Transactional
    public CloudinaryResponse uploadFile(final MultipartFile file, final String fileName) {
        try {
            final Map  result   = this.cloudinary.uploader()
                    .upload(file.getBytes(),
                            Map.of("public_id",
                                    "children/syll/"
                                            + fileName));
            final String url      = (String) result.get("secure_url");
            final String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder().publicId(publicId).url(url)
                    .build();

        } catch (final Exception e) {
            throw new BadCredentialsException("Failed to upload file");
        }
    }

    public String saveImage(MultipartFile image) {
        try {
            // Upload the image file to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("secure_url"); // Return the secure URL of the uploaded image
        } catch (IOException e) {
            throw new BadCredentialsException("Error uploading image to Cloudinary: " + e.getMessage());
        }
    }
}
