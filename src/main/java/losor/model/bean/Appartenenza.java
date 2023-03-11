package losor.model.bean;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "appartenenza")
public class Appartenenza implements Bean
{
    @Id
    @Column(name = "prodotto")
    private Integer prodotto;

    @Id
    @Column(name = "categoria")
    private Integer categoria;

    public Appartenenza() {
    }

    public Appartenenza(Integer prodotto, Integer categoria) {
        this.prodotto = prodotto;
        this.categoria = categoria;
    }

    public Integer getProdotto() {
        return this.prodotto;
    }

    public void setProdotto(Integer prodotto) {
        this.prodotto = prodotto;
    }

    public Integer getCategoria() {
        return this.categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "{" +
            " prodotto='" + getProdotto() + "'" +
            ", categoria='" + getCategoria() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Appartenenza)) {
            return false;
        }
        Appartenenza appartenenza = (Appartenenza) o;
        return Objects.equals(prodotto, appartenenza.prodotto) && Objects.equals(categoria, appartenenza.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prodotto, categoria);
    }

}
