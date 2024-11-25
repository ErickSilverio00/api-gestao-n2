package com.example.n2.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.n2.demo.entity.Usuario;
import com.example.n2.demo.repository.UsuarioRepository;
import com.example.n2.demo.responses.Response;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/usuarios")
    public List<Usuario> Get() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> GetById(@PathVariable long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (usuario.isPresent()) {
            return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Response<Usuario>> Post(@Valid @RequestBody Usuario usuario, BindingResult result) {
        Response<Usuario> response = new Response<Usuario>();
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        usuarioRepository.save(usuario);
        response.setData(usuario);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Response<Usuario>> Update(@PathVariable long id, @Valid @RequestBody Usuario usuarioNovo,
            BindingResult result) {
        Optional<Usuario> usuarioAntigo = usuarioRepository.findById(id);
        Response<Usuario> response = new Response<Usuario>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }
        if (usuarioAntigo.isPresent()) {
            Usuario usuario = usuarioAntigo.get();
            usuario.setNome(usuarioNovo.getNome());
            usuario.setSenha(usuarioNovo.getSenha());
            response.setData(usuario);
            usuarioRepository.save(usuario);
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> Delete(@PathVariable long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            usuarioRepository.delete(usuario.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
