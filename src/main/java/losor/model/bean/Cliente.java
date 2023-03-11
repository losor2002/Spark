package losor.model.bean;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "cliente")
public class Cliente implements Bean
{
    @Id @GeneratedValue
    @Column(name = "codice")
    private Integer codice;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cognome", nullable = false)
    private String cognome;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "numAcquisti", nullable = false)
    private Integer numAcquisti;

    public Cliente() {}

    public Cliente(Integer codice, String nome, String cognome, String email, String password, Integer numAcquisti) {
        this.codice = codice;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.numAcquisti = numAcquisti;
    }

    public Integer getCodice() {
        return this.codice;
    }

    public void setCodice(Integer codice) {
        this.codice = codice;
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

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getNumAcquisti() {
        return this.numAcquisti;
    }

    public void setNumAcquisti(Integer numAcquisti) {
        this.numAcquisti = numAcquisti;
    }

    @Override
    public String toString() {
        return "{" +
            " codice='" + getCodice() + "'" +
            ", nome='" + getNome() + "'" +
            ", cognome='" + getCognome() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", numAcquisti='" + getNumAcquisti() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Cliente)) {
            return false;
        }
        Cliente cliente = (Cliente) o;
        return Objects.equals(codice, cliente.codice) && Objects.equals(nome, cliente.nome) && Objects.equals(cognome, cliente.cognome) && Objects.equals(email, cliente.email) && Objects.equals(password, cliente.password) && Objects.equals(numAcquisti, cliente.numAcquisti);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codice, nome, cognome, email, password, numAcquisti);
    }

}