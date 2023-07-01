package org.example.dao;

import org.example.exceptions.DAOException;
import org.example.exceptions.TipoChaveNaoEncontradaException;
import org.example.model.Cliente;
import org.example.model.Produto;
import org.example.model.Venda;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class VendaExclusaoDAO extends GenericJpaDAO<Venda, Long> implements IVendaDAO {

    public VendaExclusaoDAO() {
        super(Venda.class);
    }

    @Override
    public void finalizarVenda(Venda venda) throws DAOException, TipoChaveNaoEncontradaException {
        super.alterar(venda);
    }

    @Override
    public void cancelarVenda(Venda venda) throws DAOException, TipoChaveNaoEncontradaException {
        super.alterar(venda);
    }

    @Override
    public Venda consultarComCollection(Long id) {
        return null;
    }


}
