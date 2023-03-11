<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, losor.model.bean.Ordine, losor.model.bean.Cliente"%>

<%
    String contextPath = application.getContextPath();
    List<Ordine> ordini = (List<Ordine>) request.getAttribute("ordini");
    List<Cliente> clienti = (List<Cliente>) request.getAttribute("clienti");
    String cliente = request.getParameter("cliente");
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= contextPath%>/images/spark-icon.ico" type="image/x-icon">
        <title>Ordini</title>
    </head>
    <body class="bg-dark text-light">
        <div class="container">
            <h1>Ordini</h1>
            <div class="row mb-3">
                <div class="col-auto d-flex align-items-end">
                    <h5>Filtra per</h5>
                </div>
                <div class="col-auto ms-lg-auto">
                    <form class="row g-3 align-items-end" action="<%= contextPath%>/adminOrdini" method="get">
                        <div class="col-auto">
                            <label for="cliente" class="form-label">Cliente</label>
                            <select class="form-select" name="cliente" id="cliente">
                                <option value=""></option>
                                <% for(Cliente c : clienti) { %>
                                    <option value="<%= c.getCodice()%>" <%= c.getCodice().toString().equals(cliente) ? "selected" : ""%>><%= c.getCodice()%>. <%= c.getNome()%> <%= c.getCognome()%></option>
                                <% } %>
                            </select>
                        </div>
                        <div class="col-auto">
                            <label for="dataInizio" class="form-label">Data inizio</label>
                            <input type="date" class="form-control" name="dataInizio" id="dataInizio" value="${param.dataInizio}">
                        </div>
                        <div class="col-auto">
                            <label for="dataFine" class="form-label">Data fine</label>
                            <input type="date" class="form-control" name="dataFine" id="dataFine" value="${param.dataFine}">
                        </div>
                        <div class="col-auto">
                            <input type="submit" class="btn btn-primary" value="Filtra">
                        </div>
                    </form>
                </div>
            </div>
            <div>
                <% for(Ordine ordine : ordini) { %>
                    <div class="card card-body bg-dark border-light mb-3">
                        <div class="row">
                            <div class="col-10">
                                Ordine #<%= ordine.getNumFattura()%><br>
                                Cliente: <%= ordine.getCliente()%>. <%= clienti.get(ordine.getCliente() - 1).getNome()%> <%= clienti.get(ordine.getCliente() - 1).getCognome()%><br>
                                Data: <%= ordine.getData()%><br>
                                Totale: <%= ordine.getPrezzoTot()%> €
                            </div>
                            <div class="col-2 d-flex align-items-center justify-content-end">
                                <a class="text-decoration-none" href="<%= contextPath%>/dettagliOrdineAdmin?numFattura=<%= ordine.getNumFattura()%>">
                                    Dettagli ordine
                                </a>
                            </div>
                        </div>
                    </div>
                <% } %>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    </body>
</html>