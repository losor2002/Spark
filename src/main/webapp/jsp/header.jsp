<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Map, java.util.List, losor.model.bean.Categoria, losor.controller.util.TextUtil"%>

<%
    String contextPath = application.getContextPath();
    Map<String, List<Categoria>> categorie = (Map<String, List<Categoria>>) request.getAttribute("categorie");
    Boolean isUserAuth = (Boolean) request.getAttribute("isUserAuth");
    String nome = (String) request.getAttribute("nome");
    String cognome = (String) request.getAttribute("cognome");
%>

<nav class="navbar navbar-expand-xl navbar-dark bg-dark sticky-top">
    <div class="container-fluid">
        <a class="navbar-brand" href="<%= contextPath%>">
            Spark
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar" aria-controls="offcanvasNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="offcanvas offcanvas-end bg-dark" tabindex="-1" id="offcanvasNavbar" aria-labelledby="offcanvasNavbarLabel">
            <div class="offcanvas-header">
                <h5 class="offcanvas-title" id="offcanvasNavbarLabel">Spark</h5>
                <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Close"></button>
            </div>
            <div class="offcanvas-body">
                <ul class="navbar-nav justify-content-start flex-grow-1 pe-3 mb-2 mb-xl-0">

                    <% for(String tipo : categorie.keySet()) { %>

                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" id="navbarDropdownMenuLink_<%= TextUtil.idEncode(tipo)%>" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <%= tipo%>
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink_<%= TextUtil.idEncode(tipo)%>">

                                <% for(Categoria categoria : categorie.get(tipo)) { %>

                                    <li><a class="dropdown-item" href="<%= contextPath%>/search?categoria=<%= categoria.getNumProgressivo()%>"><%= categoria.getSottotipo()%></a></li>

                                <% } %>

                            </ul>
                        </li>

                    <% } %>

                </ul>
                <ul class="navbar-nav justify-content-end flex-grow-1 pe-3 mb-2 mb-xl-0">

                    <% if(isUserAuth) { %>
                    
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" id="dropdown_user" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                Ciao, <%= nome%> <%= cognome%>
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="dropdown_user">
                                <li><a class="dropdown-item" href="<%= contextPath%>/areaPersonale">Area Personale</a></li>
                                <li><a class="dropdown-item" href="<%= contextPath%>/logout?who=user">Logout</a></li>
                            </ul>
                        </li>
        
                    <% } else { %>
        
                        <li class="nav-item">
                            <a class="nav-link" href="<%= contextPath%>/userLogin">Accedi</a>
                        </li>
        
                    <% } %>
        
                    <li class="nav-item d-none">
                        <a class="nav-link" href="<%= contextPath%>/wishlist">Wishlist</a>
                    </li>
        
                    <li class="nav-item">
                        <a class="nav-link" href="<%= contextPath%>/carrello">Carrello</a>
                    </li>

                </ul>
                <form class="d-flex" action="<%= contextPath%>/search" method="get">
                    <input class="form-control me-2" type="search" placeholder="Cerca" aria-label="Search" name="cerca" value="${param.cerca}">
                    <button class="btn btn-outline-primary" type="submit">Cerca</button>
                </form>
            </div>
        </div>
    </div>
</nav>