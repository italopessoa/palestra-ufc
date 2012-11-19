<%@page import="br.com.ufc.palestraufc.service.model.Lecture"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Todas As Palestras</title>
</head>
<body>
	<%@ include file="menu.jsp"%>

	<%
		ArrayList<Lecture> palestras = (ArrayList<Lecture>) session
				.getAttribute("palestras");
	%>

<%@ include file="erro.jsp" %>

	<table border="2px">
		<thead>
			<tr>
				<th>Titulo</th>
				<th>Autores</th>
				<th>Data</th>
				<th>Hora</th>
				<th>Editar</th>
				<th>Excluir</th>
			</tr>
		</thead>
		<tbody>

			<%
				for (Lecture l : palestras) {
			%>
			<tr>
				<td><%=l.getTitle()%></td>
				<td><%=l.getAuthors().toString()%></td>
				<td><%=l.getDate()%></td>
				<td><%=l.getTime()%></td>
				<td><a
					href="ServletCentral?cmd=cmdEditarPalestra&id=<%=l.getId()%>">Editar</a></td>
				<td><a
					href="ServletCentral?cmd=cmdExcluirPalestra&id=<%=l.getId()%>">Excluir</a></td>
			</tr>
			<%
				}
			%>

		</tbody>
	</table>




</body>
</html>