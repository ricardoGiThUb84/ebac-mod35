package org.example;

import org.example.dao.ClienteDAO;
import org.example.dao.IClienteDAO;
import org.example.exceptions.DAOException;
import org.example.exceptions.MaisDeUmRegistroException;
import org.example.exceptions.TableException;
import org.example.exceptions.TipoChaveNaoEncontradaException;
import org.example.model.Cliente;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteDAOTest {

    private static IClienteDAO clienteDao;

    private Random rd;

    public ClienteDAOTest() {
        this.clienteDao = new ClienteDAO();
        rd = new Random();
    }

    @AfterAll
    public static void end() throws DAOException {
        Collection<Cliente> list = clienteDao.buscarTodos();
        list.forEach(cli -> {
            try {
                clienteDao.excluir(cli);
            } catch (DAOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    @Test
    public void pesquisarCliente() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
        Cliente cliente = criarCliente();
        clienteDao.cadastrar(cliente);

        Cliente clienteConsultado = clienteDao.consultar(cliente.getId());


        assertNotNull(clienteConsultado);

    }

    @Test
    public void salvarCliente() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Cliente cliente = criarCliente();
        Cliente retorno = clienteDao.cadastrar(cliente);
        assertNotNull(retorno);

        Cliente clienteConsultado = clienteDao.consultar(retorno.getId());
        assertNotNull(clienteConsultado);

        clienteDao.excluir(cliente);

        Cliente clienteConsultado1 = clienteDao.consultar(retorno.getId());
        assertNull(clienteConsultado1);
    }

    @Test
    public void excluirCliente() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Cliente cliente = criarCliente();
        Cliente retorno = clienteDao.cadastrar(cliente);
        assertNotNull(retorno);

        Cliente clienteConsultado = clienteDao.consultar(cliente.getId());
        assertNotNull(clienteConsultado);

        clienteDao.excluir(cliente);
        clienteConsultado = clienteDao.consultar(cliente.getId());
        assertNull(clienteConsultado);
    }

    @Test
    public void alterarCliente() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Cliente cliente = criarCliente();
        Cliente retorno = clienteDao.cadastrar(cliente);
        assertNotNull(retorno);

        Cliente clienteConsultado = clienteDao.consultar(cliente.getId());
        assertNotNull(clienteConsultado);

        clienteConsultado.setNome("Ricardo Silva");
        clienteDao.alterar(clienteConsultado);

        Cliente clienteAlterado = clienteDao.consultar(clienteConsultado.getId());
        assertNotNull(clienteAlterado);
        assertEquals("Ricardo Silva", clienteAlterado.getNome());

        clienteDao.excluir(cliente);
        clienteConsultado = clienteDao.consultar(clienteAlterado.getId());
        assertNull(clienteConsultado);
    }

    @Test
    public void buscarTodos() throws TipoChaveNaoEncontradaException, DAOException {
        Cliente cliente = criarCliente();
        Cliente retorno = clienteDao.cadastrar(cliente);
        assertNotNull(retorno);

        Cliente cliente1 = criarCliente();
        Cliente retorno1 = clienteDao.cadastrar(cliente1);
        assertNotNull(retorno1);

        Collection<Cliente> list = clienteDao.buscarTodos();
        assertTrue(list != null);
        assertTrue(list.size() > 0);

        list.forEach(cli -> {
            try {
                clienteDao.excluir(cli);
            } catch (DAOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        Collection<Cliente> list1 = clienteDao.buscarTodos();
        assertTrue(list1 != null);
        assertTrue(list1.size() == 0);
    }

    private Cliente criarCliente() {
        Cliente cliente = new Cliente();
        cliente.setCpf(rd.nextLong());
        cliente.setNome("Ricardo");
        cliente.setCidade("Salvador");
        cliente.setEnd("End");
        cliente.setEstado("BA");
        cliente.setNumero(20);
        cliente.setTel(71976775645L);
        return cliente;
    }

}
