<%@page import="br.com.ufc.palestraufc.service.model.Lecture"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Editar Palestra</title>
</head>
<body>

<%@include file="menu.jsp"%>

<% 	
	Lecture l = (Lecture) session.getAttribute("palestraAEditar"); 
	String autor1 = l.getAuthors().get(0);

%>


<form action="ServletCentral" method="post" style="width:600px; margin-left: auto; margin-right: auto;">
	
	 <fieldset>
		<%@include file="erro.jsp"%>
	 	<input type="hidden" name="cmd" value="cmdSalvarPalestraEditada">
	 	<input type="hidden" name="id" value="<%=l.getId()%>">
        <label style="float: right;">* Campos obrigatórios</label><br/>
        <label>Título:</label><br />
        <input type="text" maxlength="200" name="titulo" value="<%=l.getTitle()%>"/>*<br />
        <label>Autor Principal:</label><br />
        <input type="text" maxlength="200" name="autor1" value="<%=l.getAuthors().get(0)%>"/>*<br />
        <label>Co-autores:</label><br />
        <input type="text" maxlength="200" name="autor2" <%if (l.getAuthors().size() >= 3) {%>value="<%=l.getAuthors().get(2)%>"<%} %>/><br/>
        <input type="text" maxlength="200" name="autor3" <%if (l.getAuthors().size() >= 4) {%>value="<%=l.getAuthors().get(3)%>"<%} %>/><br/>
        <input type="text" maxlength="200" name="autor4" <%if (l.getAuthors().size() >= 5) {%>value="<%=l.getAuthors().get(4)%>"<%} %>/><br/>
        <label>Orientador:</label><br />
        <input type="text" maxlength="200" name="autor5" value="<%=l.getAuthors().get(1)%>"/>*<br/>
        <label>Data:</label><br/>
        <input type="text" maxlength="50" name="data" value="<%=l.getDate()%>"/>*<br/>
        <label>Hora:</label><br />
        <input type="text" maxlength="50" name="hora" value="<%=l.getTime()%>"/>*<br/>
       </fieldset>
       <input type="submit" value="Salvar"/>	
	</form>
</body>
</html>