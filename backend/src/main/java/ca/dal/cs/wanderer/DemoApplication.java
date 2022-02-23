package ca.dal.cs.wanderer;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.Credentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.*;
import java.util.Objects;

@SpringBootApplication
public class DemoApplication{

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DemoApplication.class, args);

		ClassLoader classLoader = DemoApplication.class.getClassLoader();

		File file = new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());

		BufferedReader br = new BufferedReader(new FileReader("C:\\ME\\DAL\\Term 2\\CSCI 5308 ASDC\\Group Project\\Development\\Gitlab\\backend\\src\\main\\resources\\serviceAccountKey.json"));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}

		FileInputStream serviceAccount =
				new FileInputStream("C:\\ME\\DAL\\Term 2\\CSCI 5308 ASDC\\Group Project\\Development\\Gitlab\\backend\\src\\main\\resources\\serviceAccountKey.json");

		System.out.println(serviceAccount.toString());

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();

		FirebaseApp.initializeApp(options);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}

}
