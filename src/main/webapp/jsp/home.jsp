<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Map, java.util.List, java.util.Set, losor.model.bean.Categoria, losor.model.bean.Prodotto, losor.controller.util.TextUtil"%>

<%
    String contextPath = application.getContextPath();
    Map<Categoria, List<Prodotto>> prodottiByCategoria = (Map<Categoria, List<Prodotto>>) request.getAttribute("prodottiByCategoria");
    Set<Categoria> categorie = prodottiByCategoria.keySet();
    String sparkFiles = getServletContext().getInitParameter("sparkFiles");
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= contextPath%>/images/spark-icon.ico" type="image/x-icon">
        <title>Home</title>
    </head>
    <body class="bg-dark text-light">

        <jsp:include page="/header"/>

        <div class="container mb-5">
            <% for(Categoria categoria : categorie) { %>

                <div class="row">
                    <div class="col-10">
                        <h3><%= categoria.getTipo()%>, <%= categoria.getSottotipo()%></h3>
                    </div>
                    <div class="col-2">
                        <div class="row">
                            <div class="col-6">
                                <button class="carousel-control-prev position-static w-auto" type="button" data-bs-target="#carousel_<%= TextUtil.idEncode(categoria.getSottotipo())%>" data-bs-slide="prev">
                                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Previous</span>
                                </button>
                            </div>
                            <div class="col-6">
                                <button class="carousel-control-next position-static w-auto" type="button" data-bs-target="#carousel_<%= TextUtil.idEncode(categoria.getSottotipo())%>" data-bs-slide="next">
                                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Next</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="carousel_<%= TextUtil.idEncode(categoria.getSottotipo())%>" class="carousel slide" data-bs-interval="false">
                    <div class="carousel-inner">

                        <%
                            int i = 0;
                            boolean first = true;
                            List<Prodotto> prodotti = prodottiByCategoria.get(categoria);
                            for(Prodotto prodotto : prodotti) {
                        %>

                                <% if(i % 6 == 0) { %>

                                    <div class="carousel-item<%= first ? " active" : ""%>">
                                        <div class="row">

                                <% } %>

                                            <div class="col-4 col-xl-2 mb-2">
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

                                <% if(i % 6 == 5 || i == prodotti.size() - 1) { %>

                                        </div>
                                    </div>
                                
                                <% } %>

                        <% i++; first = false; } %>
                    </div>
                </div>
                <br>

            <% } %>
        </div>
        <%@ include file="footer.jsp"%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="<%= contextPath%>/scripts/bordoProdotti.js"></script>
    </body>
</html>