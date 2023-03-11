package losor.model.bean;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ordine")
public class Ordine implements Bean
{
    @Id @GeneratedValue
    @Column(name = "numFattura")
    private Integer numFattura;

    @Column(name = "numCarta", nullable = false)
    private String numCarta;

    @Column(name = "data", nullable = false)
    private Date data;

    @Column(name = "prezzoTot", nullable = false)
    private BigDecimal prezzoTot;

    @Column(name = "indirizzo", nullable = false)
    private Integer indirizzo;

    @Column(name = "cliente", nullable = false)
    private Integer cliente;

    public Ordine() {
    }

    public Ordine(Integer numFattura, String numCarta, Date data, BigDecimal prezzoTot, Integer indirizzo, Integer cliente) {
        this.numFattura = numFattura;
        this.numCarta = numCarta;
        this.data = data;
        this.prezzoTot = prezzoTot;
        this.indirizzo = indirizzo;
        this.cliente = cliente;
    }

    public Integer getNumFattura() {
        return this.numFattura;
    }

    public void setNumFattura(Integer numFattura) {
        this.numFattura = numFattura;
    }

    public String getNumCarta() {
        return this.numCarta;
    }

    public void setNumCarta(String numCarta) {
        this.numCarta = numCarta;
    }

    public Date getData() {
        return this.data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getPrezzoTot() {
        return this.prezzoTot;
    }

    public void setPrezzoTot(BigDecimal prezzoTot) {
        this.prezzoTot = prezzoTot;
    }

    public Integer getIndirizzo() {
        return this.indirizzo;
    }

    public void setIndirizzo(Integer indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Integer getCliente() {
        return this.cliente;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "{" +
            " numFattura='" + getNumFattura() + "'" +
            ", numCarta='" + getNumCarta() + "'" +
            ", data='" + getData() + "'" +
            ", prezzoTot='" + getPrezzoTot() + "'" +
            ", indirizzo='" + getIndirizzo() + "'" +
            ", cliente='" + getCliente() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Ordine)) {
            return false;
        }
        Ordine ordine = (Ordine) o;
        return Objects.equals(numFattura, ordine.numFattura) && Objects.equals(numCarta, ordine.numCarta) && Objects.equals(data, ordine.data) && Objects.equals(prezzoTot, ordine.prezzoTot) && Objects.equals(indirizzo, ordine.indirizzo) && Objects.equals(cliente, ordine.cliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numFattura, numCarta, data, prezzoTot, indirizzo, cliente);
    }

}
