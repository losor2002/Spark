<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    boolean delete = request.getAttribute("delete") != null;
%>

<!doctype html>

<html lang="it">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="<%= application.getContextPath()%>/images/spark-icon.ico" type="image/x-icon">
        <title>Prodotto <%= delete ? "cancellato" : "salvato"%></title>
    </head>
    <body class="bg-dark text-light">
        <div class="container">
            <div class="position-fixed top-50 start-50 translate-middle">
                <% if(request.getAttribute("SQLException") != null) { %>

                        <h1 class="text-center text-danger">Errore di comunicazione col database, operazione fallita</h1>

                <% } else { %>

                        <h1 class="text-center text-success">Il prodotto è stato <%= delete ? "cancellato" : "salvato"%> correttamente</h1>

                <% } %>

                <div class="text-center mt-3">
                    <a class="btn btn-primary" href="<%= application.getContextPath()%>/adminHome">Torna alla home dell'amministratore</a>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    </body>
</html>