package com.playlist.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.playlist.entities.Musica;
import com.playlist.entities.Playlist;
import com.playlist.entities.User;
import com.playlist.repository.MusicaRepository;
import com.playlist.repository.PlaylistRepository;
import com.playlist.repository.UserRepository;

@org.springframework.context.annotation.Configuration
public class TestConfigutation implements CommandLineRunner{

	@Autowired
	private PlaylistRepository playlistRepository;
	@Autowired
	private MusicaRepository musicaRepository;
	@Autowired 
	UserRepository userRepository;
	@Autowired
    private PasswordEncoder encoder;

	@Override
	public void run(String... args) throws Exception {
		
		Playlist p1 = new Playlist(null, "Funk RJ", "Playlist de Funk");
		Playlist p2 = new Playlist(null, "Jorge e Matheus", "Playlist de Sertanejo");
		Playlist p3 = new Playlist(null, "Melhores do Pixote", "Playlist de Pagode");
		playlistRepository.saveAll(Arrays.asList(p1,p2,p3));
		
		Musica m1 = new Musica(null, "Logo Eu", "Jorge e Matheus", "Album 2012", 2012, "Sertanejo");
		Musica m2 = new Musica(null, "Brilho no olhar", "Pixote", "Pagode do Pixote Album", 2020, "Pagode");
		Musica m3 = new Musica(null, "Freio da Blazer", "MC Cabelinho", "Trap 2022 album", 2022, "Trap");
		Musica m4 = new Musica(null, "Moves Like Jagger", "Maroon 5", "Pop Mundial", 2022, "Pop");
		musicaRepository.saveAll(Arrays.asList(m1,m2,m3, m4));
		
		User user1 = new User(null, "user@gmail.com", encoder.encode("user123"));
		userRepository.save(user1);
		
	}

}