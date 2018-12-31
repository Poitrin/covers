package covers

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class PartController {

  PartService partService
  CreativeWorkService creativeWorkService
  UtilityService utilityService

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def save(CreativeWork creativeWork, String name) {
    if (creativeWork == null) {
      notFound()
      return
    }

    def currentUserIpAddressHash = utilityService.getCurrentUserIpAddressHash(request)

    // TODO: Wie command object?
    Part part = new Part()
    part.creativeWork = creativeWork
    part.name = name
    part.ipAddressHash = currentUserIpAddressHash
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

  protected void notFound() {
      request.withFormat {
          form multipartForm {
              flash.message = message(code: 'default.not.found.message', args: [message(code: 'part.label', default: 'Part'), params.id])
              redirect action: "index", method: "GET"
          }
          '*'{ render status: NOT_FOUND }
      }
  }
}
