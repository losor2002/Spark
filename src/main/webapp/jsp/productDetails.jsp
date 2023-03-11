<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, losor.model.bean.Prodotto, losor.model.util.VersioneAggregata, losor.controller.util.ParameterUtil, losor.controller.util.TextUtil, losor.model.bean.Immagine"%>

<%
    String contextPath = application.getContextPath();
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
    List<VersioneAggregata> versioniAggregate = (List<VersioneAggregata>) request.getAttribute("versioniAggregate");
    String sparkFiles = getServletContext().getInitParameter("sparkFiles");
    String codice = request.getParameter("versione");
    VersioneAggregata versioneAggregata = null;

    if(!versioniAggregate.isEmpty())
    {
        if(!ParameterUtil.isEmpty(codice))
        {
            for(VersioneAggregata v : versioniAggregate)
            {
                if(v.getVersione().getCodice().equals(codice))
                    versioneAggregata = v;
            }
        }
        
        if(versioneAggregata == null)
            versioneAggregata = versioniAggregate.get(0);
    }
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= contextPath%>/images/spark-icon.ico" type="image/x-icon">
        <title><%= prodotto.getNome()%><%= versioneAggregata == null ? "" : " - " + versioneAggregata.getVersione().getNome()%></title>
    </head>
    <body class="bg-dark text-light">

        <jsp:include page="/header"/>

        <div class="container mb-5">
            <h1><%= prodotto.getNome()%><%= versioneAggregata == null ? "" : " - " + versioneAggregata.getVersione().getNome()%></h1>

            <% if(versioneAggregata == null) { %>

                    <h2 class="text-danger">Non ci sono versioni del prodotto da mostrare</h2>

            <% } else { %>

                    <p>
                        <span>Panoramica</span>
                        <span class="ms-5 d-none"><a class="text-decoration-none" href="<%= contextPath%>/recensioni?versione=<%= versioneAggregata.getVersione().getCodice()%>">Recensioni</a></span>
                    </p>

                    <div class="row">
                        <div class="col-12 col-xl-9">
                            <div id="carousel" class="carousel slide" data-bs-interval="false">
                                <div class="carousel-indicators">
                                    <% for(int i = 0; i < versioneAggregata.getImmagini().size(); i++) { %>

                                        <button type="button" data-bs-target="#carousel" data-bs-slide-to="<%= i%>" class="<%= i == 0 ? "active" : ""%>" <%= i == 0 ? "aria-current=\"true\"" : ""%> aria-label="Slide <%= i + 1%>"></button>

                                    <% } %>
                                </div>
                                <div class="carousel-inner">
                                    <% boolean first = true; for(Immagine immagine : versioneAggregata.getImmagini()) { %>

                                        <div class="carousel-item<%= first ? " active" : ""%>">
                                            <div class="ratio ratio-16x9">
                                                <div class="d-flex align-items-center">
                                                    <img src="<%= sparkFiles%><%= immagine.getImg()%>" class="d-block mx-auto mh-100 mw-100 h-auto w-auto" alt="immagine">
                                                </div>
                                            </div>
                                        </div>

                                    <% first = false; } %>
                                </div>
                                <button class="carousel-control-prev" type="button" data-bs-target="#carousel" data-bs-slide="prev">
                                  <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                  <span class="visually-hidden">Previous</span>
                                </button>
                                <button class="carousel-control-next" type="button" data-bs-target="#carousel" data-bs-slide="next">
                                  <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                  <span class="visually-hidden">Next</span>
                                </button>
                            </div>
                        </div>

                        <div class="col-12 col-xl-3">
                            <div class="mt-3 mt-xl-5">
                                <div class="row justify-content-around">
                                    <% for(VersioneAggregata v : versioniAggregate) { %>

                                            <div class="col-4 col-xl-6 mb-3">
                                                <form action="<%= contextPath%>/productDetails" method="get" class="text-center">
                                                    <input type="hidden" name="prodotto" value="<%= prodotto.getNumProgressivo()%>">
                                                    <input type="hidden" name="versione" value="<%= v.getVersione().getCodice()%>">
                                                    <input class="btn btn-outline-primary<%= v.getVersione().getCodice().equals(versioneAggregata.getVersione().getCodice()) ? " active" : ""%>" type="submit" value="<%= v.getVersione().getNome()%>">
                                                </form>
                                            </div>

                                    <% } %>
                                </div>

                                <p>
                                    Prezzo <span class="float-end"><%= versioneAggregata.getDatiVersione().getPrezzo()%> €</span>
                                </p>

                                <p class="d-grid gap-2">
                                    <% if(versioneAggregata.getDatiVersione().getQuantita() > 0) { %>
                                        <a class="btn btn-primary" href="<%= contextPath%>/carrello?versione=<%= versioneAggregata.getVersione().getCodice()%>&op=add">Aggiungi al carrello</a>
                                    <% } else { %>
                                        <a class="btn btn-primary disabled">Attualmente non disponibile</a>
                                    <% } %>
                                    <a class="btn btn-primary d-none" href="<%= contextPath%>/wishlist?add=<%= versioneAggregata.getVersione().getCodice()%>">Aggiungi alla wishlist</a>
                                </p>

                                <p>
                                    Produttore <span class="float-end"><%= prodotto.getProduttore()%></span>
                                </p>

                                <p>
                                    Data di uscita <span class="float-end"><%= versioneAggregata.getDatiVersione().getDataDiUscita()%></span>
                                </p>
                            </div>
                        </div>
                    </div>

                    <div class="mt-xl-3">
                        <h3>Descrizione</h3>
                        <p>
                            <%= TextUtil.htmlEncode(versioneAggregata.getDatiVersione().getDescrizione())%>
                        </p>
                    </div>

            <% } %>
        </div>
        <%@ include file="footer.jsp"%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    </body>
</html>