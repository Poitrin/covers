<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'creativeWork.label', default: 'CreativeWork')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
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

        <div id="create-creativeWork" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.creativeWork}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.creativeWork}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>

      <g:form resource="${this.creativeWork}" method="POST">
        <f:field bean="creativeWork" property="artist"/>
        <f:field bean="creativeWork" property="title"/>

        <g:submitButton name="create" class="button" value="${message(code: 'default.button.create.label', default: 'Create')}" />
      </g:form>
    </div>
  </body>
</html>
