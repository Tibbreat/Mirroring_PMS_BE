package sep490.g13.pms_be.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        final Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dhb38seql");
        config.put("api_key", "347894237995348");
        config.put("api_secret", "x0I_OBuPuN5_0H5zPMDc_8G0P38");
        return new Cloudinary(config);
    }
}
