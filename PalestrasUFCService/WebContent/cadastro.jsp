<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Inserir Nova Palestra</title>
</head>
<body>
<%@ include file="menu.jsp" %>

<form action="ServletCentral" method="post" style="width:600px; margin-left: auto; margin-right: auto;">
	
	 <fieldset>
		<%@include file="erro.jsp"%>
	 	<input type="hidden" name="cmd" value="cmdCadastrarPalestra">
        <label style="float: right;">* Campos obrigatórios</label><br/>
        <label>Título:</label><br />
        <input type="text" maxlength="200" name="titulo" />*<br />
        <label>Autor Principal:</label><br />
        <input type="text" maxlength="200" name="autor1" />*<br />
        <label>Co-autores:</label><br />
        <input type="text" maxlength="200" name="autor2"/><br/>
        <input type="text" maxlength="200" name="autor3"/><br/>
        <input type="text" maxlength="200" name="autor4"/><br/>
        <label>Orientador:</label><br />
        <input type="text" maxlength="200" name="autor5"/>*<br/>
        <label>Data:</label><br/>
        <input type="text" maxlength="50" name="data"/>*<br/>
        <label>Hora:</label><br />
        <input type="text" maxlength="50" name="hora"/>*<br/>
       </fieldset>
       <input type="submit" value="Salvar"/>	
	</form>

</body>
</html>