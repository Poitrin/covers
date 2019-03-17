<g:if test="${fieldTag != 'hidden'}">
<div class="field">
</g:if>

  <!-- TODO: Find better solution than label != 'hidden' -->
  <g:if test="${label != 'hidden'}">
  <label class="label" for="${property}">
    ${label}${required? '*' : '' }
  </label>
  </g:if>

  <div class="control ${controlClass}"
    :class="{ 'is-loading': isLoading }">
    <g:field type="text"
      class="input ${hasErrors(bean: bean, field: property, 'is-danger')}"
      name="${(prefix ?: '') + property}"
      id="${property}"
      value="${value}"
      ref="${property}"
      placeholder="${placeholder}"
      required="${required}"
      disabled="${!vueDisabled && disabled}"
      minlength="${constraints.minSize}"
      maxlength="${constraints.maxSize}"

      list="${list}"
      autocomplete="${list ? 'off' : null}"
      v-model="${property}"
      @input="${list ? 'fetch' + list : null}"
      @input="${vueInput ?: null}"
      :disabled="${vueDisabled}">
    </g:field>
    <g:if test="${list}">
      <datalist id="${list}">
        <option v-for="item in ${list}">{{ item.label }}</option>
      </datalist>
    </g:if>
  </div>

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
