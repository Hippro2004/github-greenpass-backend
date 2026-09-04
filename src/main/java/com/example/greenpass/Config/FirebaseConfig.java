package com.example.greenpass.Config;

import java.io.InputStream;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            ClassPathResource resource = new ClassPathResource("firebase-service-account.json");

            // เช็คว่าถ้ายังไม่มีไฟล์ ให้ข้ามไปก่อน ไม่ต้องฟ้อง Error สีแดง

            if (!resource.exists()) {
                System.out.println(
                        "⚠️ Warning: firebase-service-account.json not found. Push notifications are disabled.");
                return;
            }

            InputStream serviceAccount = resource.getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase initialized successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
