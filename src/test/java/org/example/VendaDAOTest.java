package org.example;

import org.example.dao.*;
import org.example.exceptions.DAOException;
import org.example.exceptions.MaisDeUmRegistroException;
import org.example.exceptions.TableException;
import org.example.exceptions.TipoChaveNaoEncontradaException;
import org.example.model.Cliente;
import org.example.model.Produto;
import org.example.model.Venda;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class VendaDAOTest {

    private static IVendaDAO vendaDao = new VendaDAO();;

    private static IVendaDAO vendaExclusaoDao = new VendaExclusaoDAO();;

    private static IClienteDAO clienteDao = new ClienteDAO();

    private static IProdutoDAO produtoDao = new ProdutoDAO();

    private  Random rd;

    private static Cliente cliente ;

    private static Produto produto;

    public VendaDAOTest() {
//        vendaDao =
//        vendaExclusaoDao
//        clienteDao
//        produtoDao =
        rd = new Random();
    }

    @BeforeAll
    public static void init() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        cliente = cadastrarCliente();
        produto = cadastrarProduto("A1", BigDecimal.TEN);
    }

    @AfterAll
    public static void end() throws DAOException {
        excluirVendas();
        excluirProdutos();
        clienteDao.excluir(cliente);
    }

    @Test
     void pesquisar() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Venda venda = criarVenda("A1");
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        Venda vendaConsultada = vendaDao.consultar(venda.getId());
        assertNotNull(vendaConsultada);
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
     void salvar() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
        Venda venda = criarVenda("A2");
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);

        assertTrue(venda.getValorTotal().equals(BigDecimal.valueOf(20)));
        assertTrue(venda.getStatus().equals(Venda.Status.INICIADA));

        Venda vendaConsultada = vendaDao.consultar(venda.getId());
        assertTrue(vendaConsultada.getId() != null);
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
    public void cancelarVenda() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A3";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        retorno.setStatus(Venda.Status.CANCELADA);
        vendaDao.cancelarVenda(venda);

        Venda vendaConsultada = vendaDao.consultar(venda.getId());
        assertEquals(codigoVenda, vendaConsultada.getCodigo());
        assertEquals(Venda.Status.CANCELADA, vendaConsultada.getStatus());
    }

    @Test
    public void adicionarMaisProdutosDoMesmo() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A4";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(produto, 1);

        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void adicionarMaisProdutosDiferentes() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A5";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        //TODO Usando este método apra evitar a exception org.hibernate.LazyInitializationException
        // Ele busca todos os dados da lista pois a mesma por default é lazy
        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);

        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }



    @Test
    public void removerProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A7";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
        valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void removerApenasUmProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A8";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
        valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void removerTodosProdutos() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A9";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerTodosProdutos();
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 0);
        assertTrue(vendaConsultada.getValorTotal().equals(BigDecimal.valueOf(0)));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
     void finalizarVenda() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A10";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        venda.setStatus(Venda.Status.CONCLUIDA);
        vendaDao.finalizarVenda(venda);

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        assertEquals(Venda.Status.CONCLUIDA, vendaConsultada.getStatus());
    }




    private static void excluirProdutos() throws DAOException {
        Collection<Produto> list = produtoDao.buscarTodos();
        list.forEach(prod -> {
            try {
                produtoDao.excluir(prod);
            } catch (DAOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    private static void excluirVendas() throws DAOException {
        Collection<Venda> list = vendaDao.buscarTodos();
        list.forEach(prod -> {
            try {
                vendaExclusaoDao.excluir(prod);
            } catch (DAOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    private static Produto cadastrarProduto(String codigo, BigDecimal valor) throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Produto produto = new Produto();
        produto.setCodigo(codigo);
        produto.setDescricao("Produto 1");
        produto.setNome("Produto 1");
        produto.setValor(valor);
        produtoDao.cadastrar(produto);
        return produto;
    }

    private static Cliente cadastrarCliente() throws TipoChaveNaoEncontradaException, DAOException {
        Cliente cliente = new Cliente();
        cliente.setCpf(new Random().nextLong());
        cliente.setNome("Ricardo");
        cliente.setCidade("Bahia");
        cliente.setEnd("End");
        cliente.setEstado("BA");
        cliente.setNumero(30);
        cliente.setTel(7199999999L);
        clienteDao.cadastrar(cliente);
        return cliente;
    }

    private Venda criarVenda(String codigo) {
        Venda venda = new Venda();
        venda.setCodigo(codigo);
        venda.setDataVenda(Instant.now());
        venda.setCliente(cliente);
        venda.setStatus(Venda.Status.INICIADA);
        venda.adicionarProduto(produto, 2);
        return venda;
    }
}
