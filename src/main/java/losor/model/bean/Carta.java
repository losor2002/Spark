package losor.model.bean;

import java.sql.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "carta")
public class Carta implements Bean
{
    @Id
    @Column(name = "numero")
    private String numero;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cognome", nullable = false)
    private String cognome;

    @Column(name = "scadenza", nullable = false)
    private Date scadenza;

    @Column(name = "cvc", nullable = false)
    private String cvc;

    @Column(name = "cliente", nullable = false)
    private Integer cliente;

    public Carta() {
    }

    public Carta(String numero, String nome, String cognome, Date scadenza, String cvc, Integer cliente) {
        this.numero = numero;
        this.nome = nome;
        this.cognome = cognome;
        this.scadenza = scadenza;
        this.cvc = cvc;
        this.cliente = cliente;
    }

    public String getNumero() {
        return this.numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public Date getScadenza() {
        return this.scadenza;
    }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
    }

    public String getCvc() {
        return this.cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
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
            " numero='" + getNumero() + "'" +
            ", nome='" + getNome() + "'" +
            ", cognome='" + getCognome() + "'" +
            ", scadenza='" + getScadenza() + "'" +
            ", cvc='" + getCvc() + "'" +
            ", cliente='" + getCliente() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Carta)) {
            return false;
        }
        Carta carta = (Carta) o;
        return Objects.equals(numero, carta.numero) && Objects.equals(nome, carta.nome) && Objects.equals(cognome, carta.cognome) && Objects.equals(scadenza, carta.scadenza) && Objects.equals(cvc, carta.cvc) && Objects.equals(cliente, carta.cliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, nome, cognome, scadenza, cvc, cliente);
    }

}
