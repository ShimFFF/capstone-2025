package naeilmolae.domain.version;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/version")
public class VersionController {

    @Tag(name = "Version")
    @GetMapping
    public ResponseEntity<String> getVersion() {
        String version = "1.0.1";
        return ResponseEntity.ok(version);
    }
}
