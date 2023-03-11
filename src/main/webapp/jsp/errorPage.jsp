<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= application.getContextPath()%>/images/spark-icon.ico" type="image/x-icon">
        <title>Errore</title>
    </head>
    <body class="bg-dark text-light">
        <jsp:include page="/header"/>
        <div class="container">
            <div class="position-fixed top-50 start-50 translate-middle">
                <h1 class="text-center mb-3">Siamo spiacenti, si è verificato un errore</h1>
                <% if(exception != null) { %>
                    <div class="text-center mb-3">
                        <div>
                            <button class="btn btn-link text-decoration-none" type="button" data-bs-toggle="collapse" data-bs-target="#collapseErrore" aria-expanded="false" aria-controls="collapseErrore">
                                Più informazioni
                            </button>
                        </div>
                        <div class="collapse" id="collapseErrore">
                            <%= exception%>
                        </div>
                    </div>
                <% } %>
                <div class="text-center">
                    <a class="btn btn-primary" href="<%= application.getContextPath()%>/home">Torna alla home</a>
                </div>
            </div>
        </div>
        <%@ include file="footer.jsp"%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    </body>
</html>