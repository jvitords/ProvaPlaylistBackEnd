package com.playlist.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playlist.entities.Musica;
import com.playlist.entities.dto.musicaDTO.MusicaPostDTO;
import com.playlist.service.MusicaService;

@WebMvcTest(MusicaController.class)
@AutoConfigureMockMvc(addFilters = false) // <- desabilita filtros de segurança
public class MusicaControllerTest  {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MusicaService musicService;

    @Autowired
    private ObjectMapper objectMapper;

    private Musica music;

    @BeforeEach
    void setup() {
        music = new Musica();
        music.setId(1L);
        music.setTitulo("Musica teste");
        music.setArtista("Artista teste");
        music.setAlbum("Album teste");
        music.setGenero("Pop");
        music.setAno(2023);
    }

    @Test
    void shouldReturnAllMusic() throws Exception {
        when(musicService.findAll()).thenReturn(List.of(music));

        mockMvc.perform(get("/musica"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].titulo").value("Musica teste"));
    }

    @Test
    void shouldReturnMusicById() throws Exception {
        when(musicService.findById(1L)).thenReturn(music);

        mockMvc.perform(get("/musica/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.titulo").value("Musica teste"));
    }

    @Test
    void shouldCreateNewMusic() throws Exception {
        MusicaPostDTO dto = new MusicaPostDTO();
        dto.setTitulo("New Music");
        dto.setArtista("Artista Teste");
        dto.setAlbum("Álbum Teste");
        dto.setAno(2024);
        dto.setGenero("Pop");

        when(musicService.fromMusica(any())).thenReturn(music);

        mockMvc.perform(post("/musica")
                .with(csrf()) 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.titulo").value("Musica teste")); 
    }


    @Test
    void shouldUpdateMusic() throws Exception {
        MusicaPostDTO dto = new MusicaPostDTO();
        dto.setTitulo("Updated Music");

        music.setTitulo("Updated Music");
        when(musicService.updateMusica(eq("Test Music"), any())).thenReturn(music);

        mockMvc.perform(put("/musica/Test Music")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.titulo").value("Updated Music"));
    }

    @Test
    void shouldDeleteMusic() throws Exception {
        doNothing().when(musicService).deleteById(1L);

        mockMvc.perform(delete("/musica/1"))
            .andExpect(status().isNoContent());
    }
}