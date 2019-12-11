package si.inspirited;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ConferenceApplicationTests {

	@Autowired
	WebApplicationContext webApplicationContext;

	@Test
	void contextLoads() {
		assertNotNull(webApplicationContext);
	}

}
