package com.example.n2.demo.entity;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    @NotEmpty(message = "O nome deve ser informado!")
    @Length(min = 5, max = 100, message = "O nome deverá ter de 5 a 100 caracteres")
    private String nome;
    @Column(nullable = false)
    @NotEmpty(message = "O telefone deve ser informado!")
    private String telefone;
    @Column(nullable = false)
    @NotEmpty(message = "O email deve ser informado!")
    private String email;
    @Column(nullable = false, unique = true)
    @NotEmpty(message = "O cnpj deve ser informado!")
    private String cnpj;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

}
