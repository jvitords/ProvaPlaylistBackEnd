package com.playlist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playlist.entities.Musica;
import com.playlist.entities.Playlist;
import com.playlist.entities.dto.playlistDTO.PlaylistPostDTO;
import com.playlist.repository.MusicaRepository;
import com.playlist.repository.PlaylistRepository;
import com.playlist.service.exception.NotFoundException;
import com.playlist.service.exception.PlaylistExistenteException;

@Service
public class PlaylistService {
	
	@Autowired
	PlaylistRepository playlistRepository;
	@Autowired
	MusicaRepository musicRepository;
	
	public PlaylistService() {
		super();
	}

	public PlaylistService(PlaylistRepository playlistRepository) {
		this.playlistRepository = playlistRepository;
	}
	
	public Playlist fromPlaylist(PlaylistPostDTO playlistDTO) {
		
		Playlist playlist = new Playlist();
		playlist.setNome(playlistDTO.getNome());
		playlist.setDescricao(playlistDTO.getDescricao());
		return playlist;
	}
	
	public List<Playlist> findAllPlaylist() { 
		
		return playlistRepository.findAll();
	}
	
	public Playlist findById(Long id) {
		
		return playlistRepository.findById(id).orElseThrow(() -> new NotFoundException("Id não encontrado."));
	}
	
	public List<Playlist> findByNameContaining(String name) {
		
		return playlistRepository.findByNomeContainingIgnoreCase(name);
	}
	
	public Playlist findByNome(String name) {
		
		return playlistRepository.findByNome(name).orElseThrow(() -> new NotFoundException("Playlist com nome" + name + "não encontrado!"));
	}
	
	public void saveNewPlaylist(Playlist playlist) {
		if(playlistRepository.existsByNomeIgnoreCase(playlist.getNome())) {
			throw new PlaylistExistenteException("Não é possivel adicionar essa playlist, pois já existe uma com esse nome.");
		}
		playlistRepository.save(playlist);
	}

	public Playlist updatePlaylist(String nome, PlaylistPostDTO playlistDto) {
	    Playlist playlist = findByNome(nome); 

	    boolean nomeBlank = playlistDto.getNome() == null || playlistDto.getNome().isBlank();
	    boolean descricaoBlank = playlistDto.getDescricao() == null || playlistDto.getDescricao().isBlank();

	    if (!nomeBlank) {
	        // Evita duplicar nome com outra playlist
	        if (!playlist.getNome().equalsIgnoreCase(playlistDto.getNome()) &&
	            playlistRepository.existsByNomeIgnoreCase(playlistDto.getNome())) {
	            throw new IllegalArgumentException("Já existe uma playlist com esse nome.");
	        }
	        playlist.setNome(playlistDto.getNome().trim());
	    }

	    if (!descricaoBlank) {
	        playlist.setDescricao(playlistDto.getDescricao().trim());
	    }
	    
	    return playlistRepository.save(playlist);
	}

	
	public void delete(String nome) {
		
		Playlist playlist = playlistRepository.findByNome(nome).orElseThrow(() -> new NotFoundException("Playlist com nome" + nome + "não encontrado!"));
		playlistRepository.delete(playlist);
	}
	
	public void addMusicInPlaylist(String nomePlaylist, String nomeMusic) {
	    Playlist playlist = playlistRepository.findByNome(nomePlaylist)
	        .orElseThrow(() -> new NotFoundException("Playlist com nome '" + nomePlaylist + "' não encontrada."));
	    
	    Musica music = musicRepository.findByTitulo(nomeMusic)
	        .orElseThrow(() -> new NotFoundException("Música com nome '" + nomeMusic + "' não encontrada."));

	    playlist.getListMusicas().add(music);
	    playlistRepository.save(playlist);
	}

	
	public void deleteMusicInPlaylist(String nomePlaylist, String nomeMusica) {
	    Playlist playlist = playlistRepository.findByNome(nomePlaylist)
	        .orElseThrow(() -> new NotFoundException("Nome da Playlist não encontrado."));

	    Musica music = musicRepository.findByTitulo(nomeMusica)
	        .orElseThrow(() -> new NotFoundException("Nome da Música não encontrado."));

	    playlist.getListMusicas().remove(music);
	    playlistRepository.save(playlist);
	}

}
