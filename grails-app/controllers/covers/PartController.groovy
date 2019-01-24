package covers

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class PartController {

  PartService partService
  CreativeWorkService creativeWorkService
  UtilityService utilityService

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
  static final int MAX_ITEMS_ALLOWED_TODAY = 500
  static final int MAX_ITEMS_ALLOWED_TODAY_BY_CURRENT_USER = 50

  def save(CreativeWork creativeWork, String name) {
    if (creativeWork == null) {
      notFound()
      return
    }

    def currentUserIpAddressHash = utilityService.getCurrentUserIpAddressHash(request)

    // TODO: How can Part information be extracted from command object?
    Part part = new Part()
    part.ipAddressHash = currentUserIpAddressHash

    if (partService.countByDateCreatedGreaterThan(utilityService.LAST_MIDNIGHT) >= MAX_ITEMS_ALLOWED_TODAY ||
      partService.countByIpAddressHashAndDateCreatedGreaterThan(currentUserIpAddressHash, utilityService.LAST_MIDNIGHT) >= MAX_ITEMS_ALLOWED_TODAY_BY_CURRENT_USER) {
      tooManyRequests(creativeWork)
      return
    }

    part.creativeWork = creativeWork
    part.name = name
    part.validate()

    if (part.hasErrors()) {
      respond part.errors, view: '/creativeWork/show', model: [
        creativeWork: creativeWork,
        suggestion: new Suggestion(),
        currentUserIpAddressHash: currentUserIpAddressHash
      ]
      return
    }

    creativeWork.addToParts(part)
    creativeWorkService.save(creativeWork)

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.created.message', args: [message(code: 'part.label', default: 'Part'), part.name])
        redirect creativeWork
      }
      '*' { respond creativeWork, [status: CREATED] }
    }
  }

  /*
  TODO: Update and delete are not usable yet via the UI
  def update(Part part) {
    if (part == null) {
      notFound()
      return
    }

    try {
      partService.save(part)
    } catch (ValidationException e) {
      respond part.errors, view:'edit'
      return
    }

    request.withFormat {
      form multipartForm {
          flash.message = message(code: 'default.updated.message', args: [message(code: 'part.label', default: 'Part'), part.id])
          redirect part
      }
      '*'{ respond part, [status: OK] }
    }
  }

  def delete(Long id) {
    if (id == null) {
      notFound()
      return
    }

    partService.delete(id)

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'part.label', default: 'Part'), id])
        redirect action:"index", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }
  */

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'part.label', default: 'Part'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }

  // TODO: more DRY (same code in PartController and SuggestionController)
  protected void tooManyRequests(creativeWork) {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.too.many.requests.message')
        redirect creativeWork
      }
      '*'{ render status: TOO_MANY_REQUESTS }
    }
  }
}
