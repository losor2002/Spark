package losor.model.bean;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "prodotto")
public class Prodotto implements Bean
{
    @Id @GeneratedValue
    @Column(name = "numProgressivo")
    private Integer numProgressivo;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "icona", nullable = true)
    private String icona;

    @Column(name = "cancellato", nullable = false)
    private Boolean cancellato;

    @Column(name = "produttore", nullable = false)
    private String produttore;

    public Prodotto() {
    }

    public Prodotto(Integer numProgressivo, String nome, String icona, Boolean cancellato, String produttore) {
        this.numProgressivo = numProgressivo;
        this.nome = nome;
        this.icona = icona;
        this.cancellato = cancellato;
        this.produttore = produttore;
    }

    public Integer getNumProgressivo() {
        return this.numProgressivo;
    }

    public void setNumProgressivo(Integer numProgressivo) {
        this.numProgressivo = numProgressivo;
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

    public Boolean isCancellato() {
        return this.cancellato;
    }

    public Boolean getCancellato() {
        return this.cancellato;
    }

    public void setCancellato(Boolean cancellato) {
        this.cancellato = cancellato;
    }

    public String getProduttore() {
        return this.produttore;
    }

    public void setProduttore(String produttore) {
        this.produttore = produttore;
    }

    @Override
    public String toString() {
        return "{" +
            " numProgressivo='" + getNumProgressivo() + "'" +
            ", nome='" + getNome() + "'" +
            ", icona='" + getIcona() + "'" +
            ", cancellato='" + isCancellato() + "'" +
            ", produttore='" + getProduttore() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Prodotto)) {
            return false;
        }
        Prodotto prodotto = (Prodotto) o;
        return Objects.equals(numProgressivo, prodotto.numProgressivo) && Objects.equals(nome, prodotto.nome) && Objects.equals(icona, prodotto.icona) && Objects.equals(cancellato, prodotto.cancellato) && Objects.equals(produttore, prodotto.produttore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numProgressivo, nome, icona, cancellato, produttore);
    }

}
