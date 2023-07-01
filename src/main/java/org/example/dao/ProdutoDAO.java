package org.example.dao;

import org.example.model.Produto;

public class ProdutoDAO extends GenericJpaDAO<Produto, Long> implements IProdutoDAO {

    public ProdutoDAO() {
        super(Produto.class);
    }

}
