<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'creativeWork.label', default: 'CreativeWork')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div id="list-creativeWork" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
      </g:if>

      <g:each in="${[null, *creativeWorkList].collate(4)}" var="row">
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