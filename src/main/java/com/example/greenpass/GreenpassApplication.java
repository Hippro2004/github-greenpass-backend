package com.example.greenpass;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class GreenpassApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenpassApplication.class, args);
	}

	@Bean
	public CommandLineRunner autoAlterTableColumns(JdbcTemplate jdbcTemplate) {
		return args -> {
			try {
				jdbcTemplate.execute("ALTER TABLE reward MODIFY COLUMN reward_details TEXT;");
				jdbcTemplate.execute("ALTER TABLE reward MODIFY COLUMN reward_title TEXT;");
				jdbcTemplate.execute("ALTER TABLE reward MODIFY COLUMN image LONGTEXT;");
				System.out.println("✅ Automatically updated reward table columns to TEXT/LONGTEXT!");
			} catch (Exception e) {
				System.err.println("⚠️ Could not alter reward table: " + e.getMessage());
			}
			try {
				jdbcTemplate.execute("ALTER TABLE announcement MODIFY COLUMN description TEXT;");
				jdbcTemplate.execute("ALTER TABLE announcement MODIFY COLUMN announcement_title TEXT;");
				jdbcTemplate.execute("ALTER TABLE announcement MODIFY COLUMN image LONGTEXT;");
				System.out.println("✅ Automatically updated announcement table columns to TEXT/LONGTEXT!");
			} catch (Exception e) {
				System.err.println("⚠️ Could not alter announcement table: " + e.getMessage());
			}
			try {
				jdbcTemplate.execute("ALTER TABLE report MODIFY COLUMN description TEXT;");
				jdbcTemplate.execute("ALTER TABLE report MODIFY COLUMN image LONGTEXT;");
			} catch (Exception e) {}
		};
	}
}
