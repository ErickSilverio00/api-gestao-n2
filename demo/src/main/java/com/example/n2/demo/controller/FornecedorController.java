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

import com.example.n2.demo.entity.Fornecedor;
import com.example.n2.demo.repository.FornecedorRepository;
import com.example.n2.demo.responses.Response;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
public class FornecedorController {
    @Autowired
    private FornecedorRepository fornecedorRepository;

    @GetMapping("/fornecedores")
    public List<Fornecedor> Get() {
        return fornecedorRepository.findAll();
    }

    @GetMapping("/fornecedores/{id}")
    public ResponseEntity<Fornecedor> GetById(@PathVariable long id) {
        Optional<Fornecedor> fornecedor = fornecedorRepository.findById(id);

        if (fornecedor.isPresent()) {
            return new ResponseEntity<>(fornecedor.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/fornecedores")
    public ResponseEntity<Response<Fornecedor>> Post(@Valid @RequestBody Fornecedor fornecedor, BindingResult result) {
        Response<Fornecedor> response = new Response<Fornecedor>();
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        if (fornecedorRepository.existsByCnpj(fornecedor.getCnpj())) {
            response.getErrors().add("Já existe um fornecedor cadastrado com este CNPJ.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        fornecedorRepository.save(fornecedor);
        response.setData(fornecedor);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/fornecedores/{id}")
    public ResponseEntity<Response<Fornecedor>> Update(@PathVariable long id,
            @Valid @RequestBody Fornecedor fornecedorNovo,
            BindingResult result) {
        Response<Fornecedor> response = new Response<Fornecedor>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Fornecedor> fornecedorAntigo = fornecedorRepository.findById(id);

        if (fornecedorAntigo.isPresent()) {
            Fornecedor fornecedor = fornecedorAntigo.get();

            if (!fornecedor.getCnpj().equals(fornecedorNovo.getCnpj())
                    && fornecedorRepository.existsByCnpj(fornecedorNovo.getCnpj())) {
                response.getErrors().add("Já existe um fornecedor cadastrado com este CNPJ.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            fornecedor.setNome(fornecedorNovo.getNome());
            fornecedor.setTelefone(fornecedorNovo.getTelefone());
            fornecedor.setEmail(fornecedorNovo.getEmail());
            fornecedor.setCnpj(fornecedorNovo.getCnpj());
            response.setData(fornecedor);
            fornecedorRepository.save(fornecedor);
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/fornecedores/{id}")
    public ResponseEntity<Void> Delete(@PathVariable long id) {
        Optional<Fornecedor> fornecedor = fornecedorRepository.findById(id);
        if (fornecedor.isPresent()) {
            fornecedorRepository.delete(fornecedor.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
