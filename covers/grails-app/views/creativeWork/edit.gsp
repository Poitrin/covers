<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'creativeWork.label', default: 'CreativeWork')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#edit-creativeWork" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="edit-creativeWork" class="content scaffold-edit" role="main">
      <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
      <div class="message is-success">
        <div class="message-body">${flash.message}</div>
      </div>
      </g:if>
      <g:hasErrors bean="${this.creativeWork}">
      <div class="message is-danger">
        <div class="message-body">
          <ul class="errors" role="alert">
            <g:eachError bean="${this.creativeWork}" var="error">
              <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
            </g:eachError>
          </ul>
        </div>
      </div>
      </g:hasErrors>
      <g:form resource="${this.creativeWork}" method="PUT">
        <f:field bean="creativeWork" property="artist"/>
        <f:field bean="creativeWork" property="title"/>

        <g:submitButton name="create" class="button" value="${message(code: 'default.button.update.label', default: 'Update')}" />
      </g:form>
    </div>
  </body>
</html>
