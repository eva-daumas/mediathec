package com.mediathec.membersservice;

import com.mediathec.membersservice.entity.Member;
import com.mediathec.membersservice.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MembersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MembersServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initAdmin(MemberService memberService) {
		return args -> {
			// Vérifier si l'admin existe déjà
			Member existingAdmin = memberService.findByEmail("admin1@email.com");

			if (existingAdmin == null) {
				// Créer l'admin
				Member admin = new Member();
				admin.setUsername("admin");
				admin.setEmail("admin1@email.com");
				admin.setPassword("123");
				admin.setRole("ADMIN");

				memberService.save(admin);

				System.out.println("========================================");
				System.out.println("✅ ADMIN CRÉÉ AVEC SUCCÈS !");
				System.out.println("   Email: admin1@email.com");
				System.out.println("   Mot de passe: 123");
				System.out.println("   Rôle: ADMIN");
				System.out.println("========================================");
			} else {
				System.out.println("ℹ️ L'admin existe déjà");
			}
		};
	}
}