<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, losor.model.bean.Carrello, losor.model.bean.Prodotto, losor.model.util.VersioneAggregata, java.math.BigDecimal"%>

<%
    String contextPath = application.getContextPath();
    String sparkFiles = getServletContext().getInitParameter("sparkFiles");
    List<Carrello> carrello = (List<Carrello>) request.getAttribute("carrello");
    List<VersioneAggregata> versioniAggregate = (List<VersioneAggregata>) request.getAttribute("versioniAggregate");
    List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("prodotti");
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= contextPath%>/images/spark-icon.ico" type="image/x-icon">
        <title>Carrello</title>
    </head>
    <body class="bg-dark text-light">
        <jsp:include page="/header"/>
        <div class="container mb-5">
            <h1>Carrello</h1>

            <div class="row">
                <div class="col-12 col-xl-9">

                    <% for(int i = 0; i < carrello.size(); i++) { Carrello carrelloObj = carrello.get(i); VersioneAggregata versioneAggregata = versioniAggregate.get(i); %>

                        <div class="card mb-3">
                            <div class="row g-0">
                              <div class="col-4">
                                <div class="ratio ratio-1x1">
                                    <div class="d-flex align-items-center">
                                        <img src="<%= sparkFiles + versioneAggregata.getVersione().getIcona()%>" class="rounded d-block me-auto mh-100 mw-100 h-auto w-auto" alt="icona">
                                    </div>
                                </div>
                              </div>
                              <div class="col-8">
                                <div class="card-body text-dark">
                                    <div class="row">
                                        <div class="col-auto">
                                            <a href="<%= contextPath%>/productDetails?prodotto=<%= versioneAggregata.getVersione().getProdotto()%>&versione=<%= carrelloObj.getVersione()%>" class="text-decoration-none">
                                                <h5 class="card-title"><%= prodotti.get(i).getNome()%></h5>
                                                <p class="card-text"><small><%= versioneAggregata.getVersione().getNome()%></small></p>
                                            </a>
                                        </div>
                                        <div class="col-auto ms-auto">
                                            <h5>
                                                <%= versioneAggregata.getDatiVersione().getPrezzo()%> €
                                            </h5>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-body text-dark position-absolute bottom-0 end-0">
                                    <p class="text-end">
                                        <a class="text-decoration-none" href="<%= contextPath%>/carrello?versione=<%= carrelloObj.getVersione()%>&op=changeAmount&amount=0">Rimuovi</a>
                                    </p>
                                    <p class="text-end d-none">
                                        <a class="text-decoration-none" href="<%= contextPath%>/wishlist?versione=<%= carrelloObj.getVersione()%>&op=add">Aggiungi alla wishlist</a>
                                    </p>
                                    <form class="row g-0" action="<%= contextPath%>/carrello" method="post">
                                        <input type="hidden" name="versione" value="<%= carrelloObj.getVersione()%>">
                                        <input type="hidden" name="op" value="changeAmount">
                                        <label for="amount" class="col-4 col-form-label text-center">Quantità</label>
                                        <div class="col-4">
                                            <input class="form-control" type="number" name="amount" id="amount" value="<%= carrelloObj.getQuantita()%>" min="0" max="<%= versioneAggregata.getDatiVersione().getQuantita()%>">
                                        </div>
                                        <div class="col-4 text-center">
                                            <input class="btn btn-link text-decoration-none" type="submit" value="Modifica">
                                        </div>
                                    </form>
                                </div>
                              </div>
                            </div>
                        </div>

                    <% } %>

                </div>
                <div class="col-12 col-xl-3">
                    <h3 class="mb-3">Riepilogo</h3>

                    <%
                        BigDecimal totale = new BigDecimal(0);
                        for(int i = 0; i < carrello.size(); i++)
                        {
                            BigDecimal quantita = new BigDecimal(carrello.get(i).getQuantita());
                            BigDecimal prezzo = versioniAggregate.get(i).getDatiVersione().getPrezzo();
                            totale = totale.add(prezzo.multiply(quantita));
                        }
                    %>

                    <p>Totale <span class="float-end"><%= totale%> €</span></p>

                    <div class="d-grid">
                        <a class="btn btn-primary<%= carrello.size() > 0 ? "" : " disabled"%>" <%= carrello.size() > 0 ? "href=\"" + contextPath + "/ordine\"" : ""%>>Procedi all'ordine</a>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="footer.jsp"%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    </body>
</html>