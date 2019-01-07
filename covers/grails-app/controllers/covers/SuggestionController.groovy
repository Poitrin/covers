package covers

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class SuggestionController {

  CreativeWorkService creativeWorkService
  PartService partService
  SuggestionService suggestionService
  UtilityService utilityService

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def save() {
    // TODO: lesen aus Command objects?
    CreativeWork creativeWork = creativeWorkService.get(params.creativeWorkId)
    Part part = partService.get(params.partId)

    Suggestion suggestion = new Suggestion()

    def currentUserIpAddressHash = utilityService.getCurrentUserIpAddressHash(request)

    // TODO: wenn aus Command objects ausgelesen wird...
    if (suggestion == null) {
      notFound()
      return
    }

    suggestion.part = part
    suggestion.instrument = params.instrument
    suggestion.ipAddressHash = currentUserIpAddressHash
    suggestion.validate() // ... so that we can extract the errors

    if (suggestion.hasErrors()) {
      respond suggestion.errors, view: '/creativeWork/show', model: [
        creativeWork: creativeWork,
        part: new Part(),
        currentUserIpAddressHash: currentUserIpAddressHash
      ]
      return
    }

    part.addToSuggestions(suggestion)
    partService.save(part)

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.created.message', args: [message(code: 'suggestion.label', default: 'Suggestion'), suggestion.instrument])
        redirect creativeWork
      }
      '*' { respond creativeWork, [status: CREATED] }
    }
  }

  def update(Suggestion suggestion) {
    if (suggestion == null) {
      notFound()
      return
    }

    try {
      suggestionService.save(suggestion)
    } catch (ValidationException e) {
      respond suggestion.errors, view:'edit'
      return
    }

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'suggestion.label', default: 'Suggestion'), suggestion.id])
        redirect suggestion
      }
      '*'{ respond suggestion, [status: OK] }
    }
  }

  def delete(Long id) {
    if (id == null) {
      notFound()
      return
    }

    suggestionService.delete(id)

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'suggestion.label', default: 'Suggestion'), id])
        redirect action:"index", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'suggestion.label', default: 'Suggestion'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }
}
