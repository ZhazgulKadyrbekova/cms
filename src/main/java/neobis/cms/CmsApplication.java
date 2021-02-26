package neobis.cms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CmsApplication {

	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args) {
		logger.info("---------- CmsApplication started ----------");
		SpringApplication.run(CmsApplication.class, args);
	}

}
