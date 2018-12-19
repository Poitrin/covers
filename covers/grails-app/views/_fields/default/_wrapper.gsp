<div class="columns">
  <div class="column is-6">
    <div class="field">
      <label class="label" for="${property}">
        ${label}${required? '*' : '' }
      </label>

      <div class="control">
        <g:field class="input ${hasErrors(bean: bean, field: property, 'is-danger')}"
          type="text"
          name="${(prefix ?: '') + property}"
          id="${property}"
          value="${value}">
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
    </div>
  </div>
</div>
