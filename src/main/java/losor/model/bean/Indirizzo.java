package losor.model.bean;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "indirizzo")
public class Indirizzo implements Bean
{
    @Id @GeneratedValue
    @Column(name = "numProgressivo")
    private Integer numProgressivo;

    @Column(name = "numCivico", nullable = false)
    private Integer numCivico;

    @Column(name = "via", nullable = false)
    private String via;

    @Column(name = "cap", nullable = false)
    private String cap;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cognome", nullable = false)
    private String cognome;

    @Column(name = "cliente", nullable = true)
    private Integer cliente;

    public Indirizzo() {
    }

    public Indirizzo(Integer numProgressivo, Integer numCivico, String via, String cap, String nome, String cognome, Integer cliente) {
        this.numProgressivo = numProgressivo;
        this.numCivico = numCivico;
        this.via = via;
        this.cap = cap;
        this.nome = nome;
        this.cognome = cognome;
        this.cliente = cliente;
    }

    public Integer getNumProgressivo() {
        return this.numProgressivo;
    }

    public void setNumProgressivo(Integer numProgressivo) {
        this.numProgressivo = numProgressivo;
    }

    public Integer getNumCivico() {
        return this.numCivico;
    }

    public void setNumCivico(Integer numCivico) {
        this.numCivico = numCivico;
    }

    public String getVia() {
        return this.via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getCap() {
        return this.cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
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
            " numProgressivo='" + getNumProgressivo() + "'" +
            ", numCivico='" + getNumCivico() + "'" +
            ", via='" + getVia() + "'" +
            ", cap='" + getCap() + "'" +
            ", nome='" + getNome() + "'" +
            ", cognome='" + getCognome() + "'" +
            ", cliente='" + getCliente() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Indirizzo)) {
            return false;
        }
        Indirizzo indirizzo = (Indirizzo) o;
        return Objects.equals(numProgressivo, indirizzo.numProgressivo) && Objects.equals(numCivico, indirizzo.numCivico) && Objects.equals(via, indirizzo.via) && Objects.equals(cap, indirizzo.cap) && Objects.equals(nome, indirizzo.nome) && Objects.equals(cognome, indirizzo.cognome) && Objects.equals(cliente, indirizzo.cliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numProgressivo, numCivico, via, cap, nome, cognome, cliente);
    }

}
