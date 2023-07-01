package org.example.dao;

import org.example.model.Cliente;

public class ClienteDAO extends GenericJpaDAO<Cliente, Long> implements IClienteDAO {

    public ClienteDAO() {
        super(Cliente.class);
    }

}