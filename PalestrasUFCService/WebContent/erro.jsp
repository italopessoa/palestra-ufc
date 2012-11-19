<div id="error_msg">
    <%
                String sucesso = (String) session.getAttribute("sucesso");
                String erro = (String) session.getAttribute("erro");
                session.removeAttribute("sucesso");
                session.removeAttribute("erro");

                if (erro != null) {%>
    <h6 style=" color: red; "align="center"><%=erro%></h6>
    <%}
                if (sucesso != null) {%>
    <h6 style="color: blue;"align="center"><%=sucesso%></h6>
    <%}%>
</div>