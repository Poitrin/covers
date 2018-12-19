<!DOCTYPE html>
<html>

<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'creativeWork.label', default: 'CreativeWork')}" />
  <title>
    <g:message code="default.show.label" args="[entityName]" />
  </title>
</head>

<body>
  <div class="columns">
    <div class="column">
      <g:link class="button" action="index">
        <!-- TODO: Translate -->
        ← Back to list
      </g:link>
    </div>
  </div>

  <div class="columns">
    <div class="column">
      <h1 class="title">${creativeWork.artist} – ${creativeWork.title}</h1>
    </div>
    <div class="column">
      <!-- TODO: SCHÖNER! -->
      <div class="is-pulled-right">
        <g:link class="button" action="edit" resource="${this.creativeWork}">
          <g:message code="default.button.edit.label" default="Edit" />
        </g:link>
        <g:form resource="${this.creativeWork}" method="DELETE" style="display: inline">
          <input class="button" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}"
            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </g:form>
      </div>
    </div>
  </div>

  <g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
  </g:if>

  
</body>

</html>