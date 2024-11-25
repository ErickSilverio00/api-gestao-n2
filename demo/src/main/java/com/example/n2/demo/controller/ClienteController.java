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

import com.example.n2.demo.entity.Cliente;
import com.example.n2.demo.repository.ClienteRepository;
import com.example.n2.demo.responses.Response;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/clientes")
    public List<Cliente> Get() {
        return clienteRepository.findAll();
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<Cliente> GetById(@PathVariable long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);

        if (cliente.isPresent()) {
            return new ResponseEntity<>(cliente.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/clientes")
    public ResponseEntity<Response<Cliente>> Post(@Valid @RequestBody Cliente cliente, BindingResult result) {
        Response<Cliente> response = new Response<Cliente>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            response.getErrors().add("Já existe um cliente cadastrado com este CPF.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        clienteRepository.save(cliente);
        response.setData(cliente);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<Response<Cliente>> Update(@PathVariable long id, @Valid @RequestBody Cliente clienteNovo,
            BindingResult result) {
        Response<Cliente> response = new Response<Cliente>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Cliente> clienteAntigo = clienteRepository.findById(id);

        if (clienteAntigo.isPresent()) {
            Cliente cliente = clienteAntigo.get();

            if (!cliente.getCpf().equals(clienteNovo.getCpf()) && clienteRepository.existsByCpf(clienteNovo.getCpf())) {
                response.getErrors().add("Já existe um cliente cadastrado com este CPF.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            cliente.setNome(clienteNovo.getNome());
            cliente.setTelefone(clienteNovo.getTelefone());
            cliente.setEmail(clienteNovo.getEmail());
            cliente.setCpf(clienteNovo.getCpf());
            clienteRepository.save(cliente);
            response.setData(cliente);
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Void> Delete(@PathVariable long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            clienteRepository.delete(cliente.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
