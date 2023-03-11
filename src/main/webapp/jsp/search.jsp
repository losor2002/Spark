<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, java.util.Map, losor.model.bean.Prodotto, losor.model.bean.Categoria, java.util.Set"%>

<%
    String contextPath = application.getContextPath();
    List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("prodotti");
    Map<String, List<Categoria>> categorie = (Map<String, List<Categoria>>) request.getAttribute("categorie");
    Set<Integer> categorieFiltrate = (Set<Integer>) request.getAttribute("categorieFiltrate");
    String sparkFiles = getServletContext().getInitParameter("sparkFiles");
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= contextPath%>/images/spark-icon.ico" type="image/x-icon">
        <title>Ricerca</title>
    </head>
    <body class="bg-dark text-light">
        <jsp:include page="/header"/>
        <div class="container mb-5">
            <h1 class="mb-3">Ricerca</h1>
            <div class="row mb-3">
                <div class="col-12 col-xl-3 mb-3">
                    <div>
                        <button class="btn btn-link text-decoration-none" type="button" data-bs-toggle="collapse" data-bs-target="#collapseCategoria" aria-expanded="false" aria-controls="collapseCategoria">
                            Filtra per categoria
                        </button>
                    </div>
                    <div class="collapse" id="collapseCategoria">
                        <form action="<%= contextPath%>/search" method="get">
                            <input type="hidden" name="cerca" value="${param.cerca}">
                            <% for(String tipo : categorie.keySet()) { %>

                                <label class="form-label"><%= tipo%></label>
                                <div class="row g-3 ms-xl-3 mb-3">
                                    <% for(Categoria categoria : categorie.get(tipo)) { %>
                                        <div class="col-auto col-xl-12">
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="categoria" value="<%= categoria.getNumProgressivo()%>" id="categoria_<%= categoria.getNumProgressivo()%>" <%= categorieFiltrate.contains(categoria.getNumProgressivo()) ? "checked" : ""%>>
                                                <label class="form-check-label" for="categoria_<%= categoria.getNumProgressivo()%>">
                                                    <%= categoria.getSottotipo()%>
                                                </label>
                                            </div>
                                        </div>
                                    <% } %>
                                </div>
                            <% } %>
                            <input type="submit" class="btn btn-primary" value="Filtra">
                        </form>
                    </div>
                </div>
                <div class="col-12 col-xl-9">
                    <div class="row g-3">
                        <% for(Prodotto prodotto : prodotti) { %>
                            <div class="col-4 col-xl-3 mb-2">
                                <a class="text-decoration-none text-dark" href="<%= contextPath%>/productDetails?prodotto=<%= prodotto.getNumProgressivo()%>">
                                    <div class="card h-100 prodotto">
                                        <div class="ratio ratio-1x1">
                                            <div class="d-flex align-items-center">
                                                <img class="rounded-top d-block mx-auto mh-100 mw-100 h-auto w-auto" src="<%= sparkFiles + prodotto.getIcona()%>" alt="icona">
                                            </div>
                                        </div>
                                        <div class="card-body">
                                            <h5 class="card-title"><%= prodotto.getNome()%></h5>
                                        </div>
                                    </div>
                                </a>
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="footer.jsp"%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="<%= contextPath%>/scripts/bordoProdotti.js"></script>
    </body>
</html>