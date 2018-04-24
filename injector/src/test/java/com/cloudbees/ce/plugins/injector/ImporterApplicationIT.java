package com.cloudbees.ce.plugins.injector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImporterApplication.class)
@ActiveProfiles(profiles = "production")
public class ImporterApplicationIT {
	@Test
	public void smokeTest() {
	}
}
