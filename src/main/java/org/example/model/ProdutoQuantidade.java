package org.example.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "TB_PRODUTO_QUANTIDADE")
public class ProdutoQuantidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Produto produto;
    private Integer quantidade;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_venda",
            foreignKey = @ForeignKey(name = "fk_produtoQtd_venda"),
            referencedColumnName = "id")
    private Venda venda;

    
    private BigDecimal valorTotal;

    public ProdutoQuantidade() {
        this.quantidade = 0;
        this.valorTotal = BigDecimal.ZERO;
    }


    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public void adicionar(Integer quantidade) {
        this.quantidade += quantidade;
        BigDecimal novoValor = this.produto.getValor().multiply(BigDecimal.valueOf(quantidade));
        BigDecimal novoTotal = this.valorTotal.add(novoValor);
        this.valorTotal = novoTotal;
    }

    public void remover(Integer quantidade) {
        this.quantidade -= quantidade;
        BigDecimal novoValor = this.produto.getValor().multiply(BigDecimal.valueOf(quantidade));
        this.valorTotal = this.valorTotal.subtract(novoValor);
    }




}