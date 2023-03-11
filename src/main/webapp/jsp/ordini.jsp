<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, losor.model.bean.Ordine"%>

<%
    String contextPath = application.getContextPath();
    List<Ordine> ordini = (List<Ordine>) request.getAttribute("ordini");
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= contextPath%>/images/spark-icon.ico" type="image/x-icon">
        <title>Area Personale | Ordini</title>
    </head>
    <body class="bg-dark text-light">
        <jsp:include page="/header"/>
        <div class="container mb-5">
            <h1 class="mb-3">Area Personale</h1>
            <p>
                <span><a class="text-decoration-none" href="<%= contextPath%>/areaPersonale">Profilo</a></span>
                <span class="ms-5"><a class="text-decoration-none" href="<%= contextPath%>/areaPersonale/indirizzi">Indirizzi</a></span>
                <span class="ms-5"><a class="text-decoration-none" href="<%= contextPath%>/areaPersonale/carte">Carte</a></span>
                <span class="ms-5">Ordini</span>
            </p>
            <div>
                <% for(Ordine ordine : ordini) { %>
                    <div class="card card-body bg-dark border-light mb-3">
                        <div class="row">
                            <div class="col-10">
                                Ordine #<%= ordine.getNumFattura()%><br>
                                Data: <%= ordine.getData()%><br>
                                Totale: <%= ordine.getPrezzoTot()%> €
                            </div>
                            <div class="col-2 d-flex align-items-center justify-content-end">
                                <a class="text-decoration-none" href="<%= contextPath%>/dettagliOrdine?numFattura=<%= ordine.getNumFattura()%>">
                                    Dettagli ordine
                                </a>
                            </div>
                        </div>
                    </div>
                <% } %>
            </div>
        </div>
        <%@ include file="footer.jsp"%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    </body>
</html>