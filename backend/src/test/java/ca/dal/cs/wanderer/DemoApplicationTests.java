package ca.dal.cs.wanderer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
		Assertions.assertEquals(6, 4+2);
		Assertions.assertEquals(6, 1+5);
	}

}
