package losor.model.bean;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "wishlist")
public class Wishlist implements Bean
{
    @Id
    @Column(name = "versione")
    private String versione;

    @Id
    @Column(name = "cliente")
    private Integer cliente;

    public Wishlist() {
    }

    public Wishlist(String versione, Integer cliente) {
        this.versione = versione;
        this.cliente = cliente;
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

    @Override
    public String toString() {
        return "{" +
            " versione='" + getVersione() + "'" +
            ", cliente='" + getCliente() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Wishlist)) {
            return false;
        }
        Wishlist wishlist = (Wishlist) o;
        return Objects.equals(versione, wishlist.versione) && Objects.equals(cliente, wishlist.cliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versione, cliente);
    }

}
