<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'creativeWork.label', default: 'CreativeWork')}" />
    <title><g:message code="creativeWork.list.label" /></title>
  </head>
  <body>
    <div id="list-creativeWork" class="content scaffold-list" role="main">
      <h1><g:message code="creativeWork.list.label" /></h1>
      <g:if test="${flash.message}">
        <div class="message is-success">
          <div class="message-body">${flash.message}</div>
        </div>
      </g:if>

      <div class="message is-info">
        <div class="message-body">
          <g:message code="default.privacy" />
        </div>
      </div>

      <g:form url="/creativeWorks" method="GET" class="field">
        <div class="field has-addons">
          <f:field class="input" type="search" bean="search" property="query" label="hidden" fieldTag="hidden" />
          <div class="control">
            <button class="button is-primary">
              <g:message code="default.search.label" />
            </button>
          </div>
        </div>
      </g:form>

      <!-- Note: When creativeWorkList is empty, "[null, *creativeWorkList]" would display 2 "Create new creative work" links -->
      <g:each in="${(creativeWorkList == null ? [null] : [null, *creativeWorkList]).collate(4)}" var="row">
      <div class="tile is-ancestor">
        <g:each in="${row}" var="creativeWork">
        <div class="tile is-parent is-3">
          <g:if test="${creativeWork == null}">
          <article class="tile is-child box notification is-primary">
            <p class="title">
              <g:link class="create" action="create" style="text-decoration: none;">
                <g:message code="default.new.label" args="[entityName]" />
              </g:link>
            </p>
          </article>
          </g:if>
          <g:else>
          <article class="tile is-child box">
            <p class="title">
              <g:link action="show" id="${creativeWork.id}">${creativeWork.title}</g:link>
            </p>
            <p class="subtitle">${creativeWork.artist}</p>
          </article>
          </g:else>
        </div>
        </g:each> <!-- row -->
      </div>
      </g:each>

      <div class="pagination">
        <g:paginate total="${creativeWorkCount ?: 0}" />
      </div>
    </div>
  </body>
</html>