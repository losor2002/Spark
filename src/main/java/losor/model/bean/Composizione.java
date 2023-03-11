package losor.model.bean;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "composizione")
public class Composizione implements Bean
{
    @Id
    @Column(name = "versione")
    private String versione;

    @Id
    @Column(name = "ordine")
    private Integer ordine;

    @Column(name = "prezzo", nullable = false)
    private BigDecimal prezzo;

    @Column(name = "quantita", nullable = false)
    private Integer quantita;

    @Column(name = "iva", nullable = false)
    private BigDecimal iva;

    public Composizione() {
    }

    public Composizione(String versione, Integer ordine, BigDecimal prezzo, Integer quantita, BigDecimal iva) {
        this.versione = versione;
        this.ordine = ordine;
        this.prezzo = prezzo;
        this.quantita = quantita;
        this.iva = iva;
    }

    public String getVersione() {
        return this.versione;
    }

    public void setVersione(String versione) {
        this.versione = versione;
    }

    public Integer getOrdine() {
        return this.ordine;
    }

    public void setOrdine(Integer ordine) {
        this.ordine = ordine;
    }

    public BigDecimal getPrezzo() {
        return this.prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public Integer getQuantita() {
        return this.quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public BigDecimal getIva() {
        return this.iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    @Override
    public String toString() {
        return "{" +
            " versione='" + getVersione() + "'" +
            ", ordine='" + getOrdine() + "'" +
            ", prezzo='" + getPrezzo() + "'" +
            ", quantita='" + getQuantita() + "'" +
            ", iva='" + getIva() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Composizione)) {
            return false;
        }
        Composizione composizione = (Composizione) o;
        return Objects.equals(versione, composizione.versione) && Objects.equals(ordine, composizione.ordine) && Objects.equals(prezzo, composizione.prezzo) && Objects.equals(quantita, composizione.quantita) && Objects.equals(iva, composizione.iva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versione, ordine, prezzo, quantita, iva);
    }

}
