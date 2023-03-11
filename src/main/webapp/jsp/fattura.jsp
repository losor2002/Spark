<?xml version="1.0" encoding="UTF-8"?>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, losor.model.bean.Ordine, losor.model.bean.Prodotto, losor.model.bean.Versione, losor.model.bean.Indirizzo, losor.model.bean.Composizione, losor.model.util.UserUtil, java.math.BigDecimal"%>

<%
    String contextPath = application.getContextPath();
    Ordine ordine = (Ordine) request.getAttribute("ordine");
    Indirizzo indirizzo = (Indirizzo) request.getAttribute("indirizzo");
    List<Composizione> composizioni = (List<Composizione>) request.getAttribute("composizioni");
    List<Versione> versioni = (List<Versione>) request.getAttribute("versioni");
    List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("prodotti");
%>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
        <style>
            @page {
                size: A4 portrait;
            }

            .float-end {
                float: right;
            }

            table, td, th {
                border: 1px solid black;
                border-collapse: collapse;
            }

            table {
                width: 100%;
            }

            td, th {
                padding: 3px;
            }
        </style>
    </head>
    <body>
        <h1>Fattura ordine #<%= ordine.getNumFattura()%></h1>
        <table>
            <thead>
                <tr>
                    <th>Intestazione fattura</th>
                    <th>Luogo di destinazione</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>
                        <%= indirizzo.getNome()%> <%= indirizzo.getCognome()%><br/>
                        <%= indirizzo.getVia()%>, <%= indirizzo.getNumCivico()%><br/>
                        <%= indirizzo.getCap()%>
                    </td>
                    <td>
                        <%= indirizzo.getNome()%> <%= indirizzo.getCognome()%><br/>
                        <%= indirizzo.getVia()%>, <%= indirizzo.getNumCivico()%><br/>
                        <%= indirizzo.getCap()%>
                    </td>
                </tr>
            </tbody>
        </table>
        <br/>
        <table>
            <thead>
                <tr>
                    <th>Numero documento</th>
                    <th>Data documento</th>
                    <th>Codice cliente</th>
                    <th>Modalità di pagamento</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><%= ordine.getNumFattura()%></td>
                    <td><%= ordine.getData()%></td>
                    <td><%= UserUtil.getUserId(session).get()%></td>
                    <td>Carta di credito: *<%= ordine.getNumCarta().substring(ordine.getNumCarta().length() - 4)%></td>
                </tr>
            </tbody>
        </table>
        <br/>
        <table>
            <thead>
                <tr>
                    <th>Codice</th>
                    <th>Descrizione</th>
                    <th>Q.tà</th>
                    <th>Prezzo</th>
                    <th>Importo</th>
                    <th>IVA</th>
                </tr>
            </thead>
            <tbody>
                <% for(int i = 0; i < composizioni.size(); i++) { Composizione composizione = composizioni.get(i); Versione versione = versioni.get(i); Prodotto prodotto = prodotti.get(i);%>
                    <tr>
                        <td><%= composizione.getVersione()%></td>
                        <td><%= prodotto.getNome()%> - <%= versione.getNome()%></td>
                        <td><%= composizione.getQuantita()%></td>
                        <td>€ <%= composizione.getPrezzo()%></td>
                        <td>€ <%= composizione.getPrezzo().multiply(new BigDecimal(composizione.getQuantita()))%></td>
                        <td><%= composizione.getIva()%></td>
                    </tr>
                <% } %>
            </tbody>
        </table>
        <table>
            <tr>
                <td>Totale importo <span class="float-end">€ <%= ordine.getPrezzoTot()%></span></td>
            </tr>
        </table>
    </body>
</html>