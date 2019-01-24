package covers

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*

class PartControllerSpec extends Specification implements ControllerUnitTest<PartController>, DomainUnitTest<Part> {
  static final MOCK_PART_NAME = 'Piano at the beginning of the song'
  static final MOCK_IP_ADDRESS_HASH = '-1234567'

  def populateValidParams(params) {
    assert params != null
    params["name"] = MOCK_PART_NAME
  }

  CreativeWork createCreativeWork() {
    CreativeWork creativeWork = new CreativeWork(
      title: 'Lady Madonna',
      artist: 'The Beatles',
      ipAddressHash: MOCK_IP_ADDRESS_HASH)
    creativeWork.save()
    return creativeWork
  }

  void "Test the index action does not exist"() {
    when:"The index action is executed"
    controller.index()

    then:"An error is thrown because the method does not exist"
    thrown MissingMethodException
  }

  void "Test the create action does not exist"() {
    when:"The create action is executed"
    controller.create()

    then:"An error is thrown because the method does not exist"
    thrown MissingMethodException
  }

  void "Test the save action with a null instance"() {
    when:"Save is called for a domain instance that doesn't exist"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'POST'
    controller.save(null, "part name")

    then:"A 404 error is returned"
    response.redirectedUrl == '/part/index'
    flash.message != null
  }

  void "Test the save action correctly persists"() {
    given:
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * save(_ as CreativeWork)
    }
    controller.partService = Mock(PartService) {
      1 * countByDateCreatedGreaterThan(_) >> 0
      1 * countByIpAddressHashAndDateCreatedGreaterThan(_, _) >> 0
    }
    controller.utilityService = Mock(UtilityService) {
      1 * getCurrentUserIpAddressHash(_) >> MOCK_IP_ADDRESS_HASH
    }

    when:"The save action is executed with a valid instance"
    response.reset()
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'POST'

    CreativeWork creativeWork = createCreativeWork()
    controller.save(creativeWork, MOCK_PART_NAME)

    then:"A redirect is issued to the show action"
    response.redirectedUrl == ('/creativeWorks/' + creativeWork.id)
    controller.flash.message != null
  }

  void "Test the save action with an invalid instance"() {
    given:
    controller.partService = Mock(PartService) {
      1 * countByDateCreatedGreaterThan(_) >> 0
      1 * countByIpAddressHashAndDateCreatedGreaterThan(_, _) >> 0
    }
    controller.utilityService = Mock(UtilityService) {
      1 * getCurrentUserIpAddressHash(_) >> MOCK_IP_ADDRESS_HASH
    }

    when:"The save action is executed with an invalid instance"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'POST'

    controller.save(createCreativeWork(), "")

    then:"The creativeWork/show view is rendered again with the correct model"
    model.creativeWork != null
    model.part != null
    view == '/creativeWork/show'
  }

  void "Test the save action does not allow too many inserts per day"() {
    given:
    controller.partService = Mock(PartService) {
      1 * countByDateCreatedGreaterThan(_) >> PartController.MAX_ITEMS_ALLOWED_TODAY
      0 * countByIpAddressHashAndDateCreatedGreaterThan(_, _) >> 0
      0 * save(_ as Part)
    }
    controller.utilityService = Mock(UtilityService) {
      1 * getCurrentUserIpAddressHash(_) >> "-12345"
    }

    when:"The save action is executed with a valid instance"
    response.reset()
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'POST'

    CreativeWork creativeWork = createCreativeWork()
    controller.save(creativeWork, MOCK_PART_NAME)

    then:"A redirect is issued to the show action"
    response.redirectedUrl == ('/creativeWorks/' + creativeWork.id)
    controller.flash.message == 'default.too.many.requests.message'
  }

  void "Test the save action does not allow too many inserts by the same user per day"() {
    given:
    controller.partService = Mock(PartService) {
      1 * countByDateCreatedGreaterThan(_) >> (PartController.MAX_ITEMS_ALLOWED_TODAY - 10)
      1 * countByIpAddressHashAndDateCreatedGreaterThan(_, _) >> PartController.MAX_ITEMS_ALLOWED_TODAY_BY_CURRENT_USER
      0 * save(_ as Part)
    }
    controller.utilityService = Mock(UtilityService) {
      1 * getCurrentUserIpAddressHash(_) >> "-12345"
    }

    when:"The save action is executed with a valid instance"
    response.reset()
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'POST'

    CreativeWork creativeWork = createCreativeWork()
    controller.save(creativeWork, MOCK_PART_NAME)

    then:"A redirect is issued to the show action"
    response.redirectedUrl == ('/creativeWorks/' + creativeWork.id)
    controller.flash.message == 'default.too.many.requests.message'
  }

  void "Test the show action with a null id"() {
    when:"The show action is executed with a null domain"
    controller.show(null)

    then:"An error is thrown because the method does not exist"
    thrown MissingMethodException
  }

  void "Test the show action with a valid id"() {
    when:"A domain instance is passed to the show action"
    controller.show(2)

    then:"An error is thrown because the method does not exist"
    thrown MissingMethodException
  }

  void "Test the edit action with a null id"() {
    when:"The show action is executed with a null domain"
    controller.edit(null)

    then:"An error is thrown because the method does not exist"
    thrown MissingMethodException
  }

  void "Test the edit action with a valid id"() {
    when:"A domain instance is passed to the show action"
    controller.edit(2)

    then:"An error is thrown because the method does not exist"
    thrown MissingMethodException
  }

  /*
  TODO: Update and delete are not usable yet via the UI
  void "Test the update action with a null instance"() {
    when:"Save is called for a domain instance that doesn't exist"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'PUT'
    controller.update(null)

    then:"A 404 error is returned"
    response.redirectedUrl == '/part/index'
    flash.message != null
  }

  void "Test the update action correctly persists"() {
    given:
    controller.partService = Mock(PartService) {
      1 * save(_ as Part)
    }

    when:"The save action is executed with a valid instance"
    response.reset()
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'PUT'
    populateValidParams(params)
    def part = new Part(params)
    part.id = 1

    controller.update(part)

    then:"A redirect is issued to the show action"
    response.redirectedUrl == '/part/show/1'
    controller.flash.message != null
  }

  void "Test the update action with an invalid instance"() {
    given:
    controller.partService = Mock(PartService) {
      1 * save(_ as Part) >> { Part part ->
        throw new ValidationException("Invalid instance", part.errors)
      }
    }

    when:"The save action is executed with an invalid instance"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'PUT'
    controller.update(new Part())

    then:"The edit view is rendered again with the correct model"
    model.part != null
    view == 'edit'
  }

  void "Test the delete action with a null instance"() {
    when:"The delete action is called for a null instance"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'DELETE'
    controller.delete(null)

    then:"A 404 is returned"
    response.redirectedUrl == '/part/index'
    flash.message != null
  }

  void "Test the delete action with an instance"() {
    given:
    controller.partService = Mock(PartService) {
      1 * delete(2)
    }

    when:"The domain instance is passed to the delete action"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'DELETE'
    controller.delete(2)

    then:"The user is redirected to index"
    response.redirectedUrl == '/part/index'
    flash.message != null
  }
  */
}






