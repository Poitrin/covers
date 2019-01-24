package covers

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class CreativeWorkController {

  CreativeWorkService creativeWorkService
  UtilityService utilityService

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
  static final int MAX_ITEMS_ALLOWED_TODAY = 500
  static final int MAX_ITEMS_ALLOWED_TODAY_BY_CURRENT_USER = 50

  def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    params.sort = "id"
    params.order = "desc"

    def currentUserIpAddressHash = utilityService.getCurrentUserIpAddressHash(request)

    respond creativeWorkService.findAllByApprovedOrIpAddressHash(true, currentUserIpAddressHash),
      model: [creativeWorkCount: creativeWorkService.count()]
  }

  def show(Long id) {
    respond creativeWorkService.get(id), model: [
      part: new Part(),
      suggestion: new Suggestion(),
      currentUserIpAddressHash: utilityService.getCurrentUserIpAddressHash(request)
    ]
  }

  def create() {
    respond new CreativeWork(params)
  }

  def save(CreativeWork creativeWork) {
    if (creativeWork == null) {
      notFound()
      return
    }

    creativeWork.ipAddressHash = utilityService.getCurrentUserIpAddressHash(request)

    if (creativeWorkService.countByDateCreatedGreaterThan(utilityService.LAST_MIDNIGHT) >= MAX_ITEMS_ALLOWED_TODAY ||
      creativeWorkService.countByIpAddressHashAndDateCreatedGreaterThan(creativeWork.ipAddressHash, utilityService.LAST_MIDNIGHT) >= MAX_ITEMS_ALLOWED_TODAY_BY_CURRENT_USER) {
      tooManyRequests()
      return
    }

    try {
      creativeWorkService.save(creativeWork)
    } catch (ValidationException e) {
      respond creativeWork.errors, view: 'create'
      return
    }

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.created.message', args: [message(code: 'creativeWork.label', default: 'CreativeWork'), creativeWork.id])
        redirect creativeWork
      }
      '*' { respond creativeWork, [status: CREATED] }
    }
  }

  /*
  TODO: Update and delete are not usable yet via the UI
  def edit(Long id) {
    respond creativeWorkService.get(id)
  }

  def update(CreativeWork creativeWork) {
    if (creativeWork == null) {
        notFound()
        return
    }

    try {
        creativeWorkService.save(creativeWork)
    } catch (ValidationException e) {
        respond creativeWork.errors, view:'edit'
        return
    }

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'creativeWork.label', default: 'CreativeWork'), creativeWork.id])
        redirect creativeWork
      }
      '*'{ respond creativeWork, [status: OK] }
    }
  }

  def delete(Long id) {
    if (id == null) {
      notFound()
      return
    }

    creativeWorkService.delete(id)

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'creativeWork.label', default: 'CreativeWork'), id])
        redirect action:"index", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }
  */

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'creativeWork.label', default: 'CreativeWork'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }

  // TODO: more DRY (same code in PartController and SuggestionController)
  protected void tooManyRequests() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.too.many.requests.message')
        redirect action: "index", method: "GET"
      }
      '*'{ render status: TOO_MANY_REQUESTS }
    }
  }
}
