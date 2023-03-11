package losor.model.bean;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "carrello")
public class Carrello implements Bean
{
    @Id
    @Column(name = "versione")
    private String versione;

    @Id
    @Column(name = "cliente")
    private Integer cliente;

    @Column(name = "quantita", nullable = false)
    private Integer quantita;

    public Carrello() {
    }

    public Carrello(String versione, Integer cliente, Integer quantita) {
        this.versione = versione;
        this.cliente = cliente;
        this.quantita = quantita;
    }

    public String getVersione() {
        return this.versione;
    }

    public void setVersione(String versione) {
        this.versione = versione;
    }

    public Integer getCliente() {
        return this.cliente;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

    public Integer getQuantita() {
        return this.quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    @Override
    public String toString() {
        return "{" +
            " versione='" + getVersione() + "'" +
            ", cliente='" + getCliente() + "'" +
            ", quantita='" + getQuantita() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Carrello)) {
            return false;
        }
        Carrello carrello = (Carrello) o;
        return Objects.equals(versione, carrello.versione) && Objects.equals(cliente, carrello.cliente) && Objects.equals(quantita, carrello.quantita);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versione, cliente, quantita);
    }

}
