<g:if test="${fieldTag != 'hidden'}">
<div class="field">
</g:if>

  <!-- TODO: Find better solution than label != 'hidden' -->
  <g:if test="${label != 'hidden'}">
  <label class="label" for="${property}">
    ${label}${required? '*' : '' }
  </label>
  </g:if>

  <div class="control">
    <g:field class="input ${hasErrors(bean: bean, field: property, 'is-danger')}"
      type="text"
      name="${(prefix ?: '') + property}"
      id="${property}"
      value="${value}"
      placeholder="${placeholder}">
    </g:field>
  </div>

  <!-- TODO: <g:if test="${required}">required</g:if>-->

  <g:hasErrors bean="${bean}" field="${property}">
    <g:eachError bean="${bean}" field="${property}" as="list">
      <p class="help is-danger">
        <g:message error="${it}" />
      </p>
    </g:eachError>
  </g:hasErrors>

<g:if test="${fieldTag != 'hidden'}">
</div>
</g:if>
