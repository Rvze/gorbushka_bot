package recognitioncommons.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("recognitioncommons")
@EntityScan("recognitioncommons")
class RecognitionLibraryConfiguration