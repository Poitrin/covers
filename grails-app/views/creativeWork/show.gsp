<!DOCTYPE html>
<html>

<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'creativeWork.label', default: 'CreativeWork')}" />
  <title>
    <g:message code="creativeWork.show.label" args="${[creativeWork.artist + ' – ' + creativeWork.title]}" />
  </title>
</head>

<body>
  <div class="columns">
    <div class="column">
      <g:link class="button" action="index" controller="creativeWork">
        ← <g:message code="default.backToList" default="Back to list" />
      </g:link>
    </div>
  </div>

  <div class="columns">
    <div class="column">
      <h1 class="title">${creativeWork.artist} – ${creativeWork.title}</h1>
    </div>
    <!-- TODO: as soon as admin features / login are available: -->
    <g:if test="${false}">
    <div class="column">
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
    </g:if>
  </div>

  <g:if test="${flash.message}">
    <div class="message is-success">
      <div class="message-body">${flash.message}</div>
    </div>
  </g:if>

  <!-- TODO: DRY!!! -->
  <g:hasErrors bean="${this.part}">
  <div class="message is-danger">
    <div class="message-body">
      <ul class="errors" role="alert">
        <g:eachError bean="${this.part}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
        </g:eachError>
      </ul>
    </div>
  </div>
  </g:hasErrors>

  <g:hasErrors bean="${this.suggestion}">
  <div class="message is-danger">
    <div class="message-body">
      <ul class="errors" role="alert">
        <g:eachError bean="${this.suggestion}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
        </g:eachError>
      </ul>
    </div>
  </div>
  </g:hasErrors>

  <g:if test="${creativeWork.youtubeVideoId}">
  <div class="columns">
    <div class="column is-12">
      <div class="box">
        <div id="player" data-youtube-video-id="${creativeWork.youtubeVideoId}">
        </div>
      </div>
    </div>
  </div>
  </g:if>

  <g:each in="${[*creativeWork.approvedParts(currentUserIpAddressHash), null]}" var="part">
  <div class="columns">
    <div class="column">
      <div class="box">
        <g:if test="${part}">
        <p class="title">
          <a class="timing" data-timing="${part.timing}">${part.timingHumanReadable}</a>&nbsp;${part.name}
        </p>
        <!-- Suggestions: -->
        <g:each in="${[null, *part.approvedSuggestions(currentUserIpAddressHash)].collate(3)}" var="row">
        <div class="tile is-ancestor">
          <g:each in="${row}" var="suggestion">
          <div class="tile is-parent is-4 suggestion">
            <g:if test="${suggestion}">
            <div class="tile is-child box">
              ${suggestion.instrument}
            </div>
            </g:if>
            <g:else>
              <!-- Field is displayed when user hasn't submitted data yet
                OR when it's the field that has been submitted -->
              <g:if test="${!params.partId || (params.partId.toInteger() == part.id)}">
              <g:form url="/creativeWorks/${creativeWork.id}/parts/${part.id}/suggestions" method="POST" style="width: 100%">
                <g:hiddenField name="partId" value="${part.id}" />
                <f:field bean="suggestion" property="instrument" widget="textarea"
                  placeholder="${message(code: 'suggestion.instrument.label')}"
                  label="hidden" />

                <g:submitButton name="create" class="button" value="${message(code: 'default.button.create.label', default: 'Create')}" />
              </g:form>
              </g:if>
            </g:else>
          </div>
          </g:each>
        </div>
        </g:each>
        </g:if>
        <g:else>
        <g:form url="/creativeWorks/${creativeWork.id}/parts" method="POST">
          <div class="columns">
            <div class="column is-8">
              <div class="field is-grouped">
                <g:hiddenField name="creativeWorkId" value="${creativeWork.id}" />
                <f:field bean="part" property="timingHumanReadable" placeholder="${message(code: 'part.timingHumanReadable.label')}" label="hidden" fieldTag="hidden" />
                <f:field bean="part" property="name" widget="textarea" placeholder="${message(code: 'part.name.label')}" label="hidden" fieldTag="hidden" controlClass="is-expanded" />
              </div>

              <g:submitButton name="create" class="button" value="${message(code: 'default.button.create.label', default: 'Create')}" />
            </div>
          </div>
        </g:form>
        </g:else>
      </div>
    </div>
  </div>
  </g:each>

  <content tag="footscripts">
    <asset:javascript src="show-creativeWork.js" />
  </content>
</body>

</html>