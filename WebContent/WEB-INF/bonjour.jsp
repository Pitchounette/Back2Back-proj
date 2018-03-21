<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Test</title>
</head>
<body>
<p>Bonjour à vous !</p>
<p>
    <%
        String heure = (String) request.getAttribute("heure");
        if (heure.equals("jour")) {
            out.println("Bonjour"); 
        }
        else {
            out.println("Bonsoir");
        }
    %>
</p>
<p>
    <%
        for (int i = 0 ; i < 20 ; i++) {
            out.println("Une nouvelle ligne !<br />");
        }
    %>
</p>
</body>
</html>