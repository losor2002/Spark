package losor.model.bean;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "versione")
public class Versione implements Bean
{
    @Id
    @Column(name = "codice")
    private String codice;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "icona", nullable = true)
    private String icona;

    @Column(name = "cancellata", nullable = false)
    private Boolean cancellata;

    @Column(name = "prodotto", nullable = false)
    private Integer prodotto;

    public Versione() {
    }

    public Versione(String codice, String nome, String icona, Boolean cancellata, Integer prodotto) {
        this.codice = codice;
        this.nome = nome;
        this.icona = icona;
        this.cancellata = cancellata;
        this.prodotto = prodotto;
    }

    public String getCodice() {
        return this.codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIcona() {
        return this.icona;
    }

    public void setIcona(String icona) {
        this.icona = icona;
    }

    public Boolean isCancellata() {
        return this.cancellata;
    }

    public Boolean getCancellata() {
        return this.cancellata;
    }

    public void setCancellata(Boolean cancellata) {
        this.cancellata = cancellata;
    }

    public Integer getProdotto() {
        return this.prodotto;
    }

    public void setProdotto(Integer prodotto) {
        this.prodotto = prodotto;
    }

    @Override
    public String toString() {
        return "{" +
            " codice='" + getCodice() + "'" +
            ", nome='" + getNome() + "'" +
            ", icona='" + getIcona() + "'" +
            ", cancellata='" + isCancellata() + "'" +
            ", prodotto='" + getProdotto() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Versione)) {
            return false;
        }
        Versione versione = (Versione) o;
        return Objects.equals(codice, versione.codice) && Objects.equals(nome, versione.nome) && Objects.equals(icona, versione.icona) && Objects.equals(cancellata, versione.cancellata) && Objects.equals(prodotto, versione.prodotto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codice, nome, icona, cancellata, prodotto);
    }

}
