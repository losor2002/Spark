<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, losor.model.bean.Cliente"%>

<%
    String contextPath = application.getContextPath();
    List<Cliente> clienti = (List<Cliente>) request.getAttribute("clienti");
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= contextPath%>/images/spark-icon.ico" type="image/x-icon">
        <title>Utenti</title>
    </head>
    <body class="bg-dark text-light">
        <div class="container">
            <h1>Utenti</h1>
            <div class="row mb-3">
                <div class="col-auto d-flex align-items-center">
                    <h5 class="mb-0">Filtra per</h5>
                </div>
                <div class="col-auto ms-auto">
                    <form class="row g-3 align-items-end" action="<%= contextPath%>/utenti" method="get">
                        <div class="col-auto">
                            <input type="email" class="form-control" name="email" value="${param.email}" placeholder="Email">
                        </div>
                        <div class="col-auto">
                            <input type="submit" class="btn btn-primary" value="Filtra">
                        </div>
                    </form>
                </div>
            </div>
            <div>
                <% for(Cliente cliente : clienti) { %>
                    <div class="card card-body bg-dark border-light mb-3">
                        <div class="row">
                            <div class="col-10">
                                Utente #<%= cliente.getCodice()%><br>
                                Nome: <%= cliente.getNome()%><br>
                                Cognome: <%= cliente.getCognome()%><br>
                                Email: <%= cliente.getEmail()%><br>
                                Numero Ordini: <%= cliente.getNumAcquisti()%>
                            </div>
                            <div class="col-2 d-flex align-items-center justify-content-end">
                                <a class="text-decoration-none" href="<%= contextPath%>/adminOrdini?cliente=<%= cliente.getCodice()%>">
                                    Visualizza ordini
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