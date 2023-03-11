package losor.model.bean;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "produttore")
public class Produttore implements Bean
{
    @Id
    @Column(name = "nome")
    private String nome;

    public Produttore() {
    }

    public Produttore(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "{" +
            " nome='" + getNome() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Produttore)) {
            return false;
        }
        Produttore produttore = (Produttore) o;
        return Objects.equals(nome, produttore.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nome);
    }

}
