package covers

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*

class CreativeWorkControllerSpec extends Specification implements ControllerUnitTest<CreativeWorkController>, DomainUnitTest<CreativeWork> {

  def populateValidParams(params) {
    assert params != null

    params["title"] = 'The Show Must Go On'
    params["artist"] = 'Queen'
  }

  void "Test the index action returns the correct model"() {
    given:
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * findAllByApprovedOrIpAddressHash(_, _) >> []
      1 * count() >> 0
    }
    controller.utilityService = Mock(UtilityService) {
      1 * getCurrentUserIpAddressHash(_) >> "-12345"
    }

    when:"The index action is executed"
    controller.index()

    then:"The model is correct"
    !model.creativeWorkList
    model.creativeWorkCount == 0
  }

  void "Test the create action returns the correct model"() {
    when:"The create action is executed"
    controller.create()

    then:"The model is correctly created"
    model.creativeWork!= null
  }

  void "Test the save action with a null instance"() {
    when:"Save is called for a domain instance that doesn't exist"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'POST'
    controller.save(null)

    then:"A 404 error is returned"
    response.redirectedUrl == '/creativeWorks'
    flash.message != null
  }

  void "Test the save action correctly persists"() {
    given:
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * save(_ as CreativeWork)
    }
    controller.utilityService = Mock(UtilityService) {
      1 * getCurrentUserIpAddressHash(_) >> "-12345"
    }

    when:"The save action is executed with a valid instance"
    response.reset()
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'POST'
    populateValidParams(params)
    def creativeWork = new CreativeWork(params)
    creativeWork.id = 1

    controller.save(creativeWork)

    then:"A redirect is issued to the show action"
    response.redirectedUrl == '/creativeWorks/1'
    controller.flash.message != null
  }

  void "Test the save action with an invalid instance"() {
    given:
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * save(_ as CreativeWork) >> { CreativeWork creativeWork ->
        throw new ValidationException("Invalid instance", creativeWork.errors)
      }
    }
    controller.utilityService = Mock(UtilityService) {
      // TODO: DRYer
      1 * getCurrentUserIpAddressHash(_) >> "-12345"
    }

    when:"The save action is executed with an invalid instance"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'POST'
    def creativeWork = new CreativeWork()
    controller.save(creativeWork)

    then:"The create view is rendered again with the correct model"
    model.creativeWork != null
    view == 'create'
  }

  void "Test the save action does not allow too many inserts per day"() {
    given:
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * countByDateCreatedGreaterThan(_) >> CreativeWorkController.MAX_ITEMS_ALLOWED_TODAY
      0 * countByIpAddressHashAndDateCreatedGreaterThan(_, _) >> 0
      0 * save(_ as CreativeWork)
    }
    controller.utilityService = Mock(UtilityService) {
      // TODO: DRYer
      1 * getCurrentUserIpAddressHash(_) >> "-12345"
    }

    when:"The save action is executed with a valid instance"
    response.reset()
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'POST'
    populateValidParams(params)
    def creativeWork = new CreativeWork(params)
    creativeWork.id = 1

    controller.save(creativeWork)

    then:"A redirect is issued to the index action"
    response.redirectedUrl == '/creativeWorks'
    controller.flash.message == 'default.too.many.requests.message'
  }

  void "Test the save action does not allow too many inserts by the same user per day"() {
    given:
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * countByDateCreatedGreaterThan(_) >> (CreativeWorkController.MAX_ITEMS_ALLOWED_TODAY - 10)
      1 * countByIpAddressHashAndDateCreatedGreaterThan(_, _) >> CreativeWorkController.MAX_ITEMS_ALLOWED_TODAY_BY_CURRENT_USER
      0 * save(_ as CreativeWork)
    }
    controller.utilityService = Mock(UtilityService) {
      1 * getCurrentUserIpAddressHash(_) >> "-12345"
    }

    when:"The save action is executed with a valid instance"
    response.reset()
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'POST'
    populateValidParams(params)
    def creativeWork = new CreativeWork(params)
    creativeWork.id = 1

    controller.save(creativeWork)

    then:"A redirect is issued to the index action"
    response.redirectedUrl == '/creativeWorks'
    controller.flash.message == 'default.too.many.requests.message'
  }

  void "Test the show action with a null id"() {
    given:
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * get(null) >> null
    }
    controller.utilityService = Mock(UtilityService) {
      1 * getCurrentUserIpAddressHash(_) >> "-12345"
    }

    when:"The show action is executed with a null domain"
    controller.show(null)

    then:"A 404 error is returned"
    response.status == 404
  }

  void "Test the show action with a valid id"() {
    given:
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * get(2) >> new CreativeWork()
    }
    controller.utilityService = Mock(UtilityService) {
      1 * getCurrentUserIpAddressHash(_) >> "-12345"
    }

    when:"A domain instance is passed to the show action"
    controller.show(2)

    then:"A model is populated containing the domain instance"
    model.creativeWork instanceof CreativeWork
  }

  /*
  TODO: Update and delete are not usable yet via the UI
  void "Test the edit action with a null id"() {
    given:
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * get(null) >> null
    }

    when:"The show action is executed with a null domain"
    controller.edit(null)

    then:"A 404 error is returned"
    response.status == 404
  }

  void "Test the edit action with a valid id"() {
    given:
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * get(2) >> new CreativeWork()
    }

    when:"A domain instance is passed to the show action"
    controller.edit(2)

    then:"A model is populated containing the domain instance"
    model.creativeWork instanceof CreativeWork
  }


  void "Test the update action with a null instance"() {
    when:"Save is called for a domain instance that doesn't exist"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'PUT'
    controller.update(null)

    then:"A 404 error is returned"
    response.redirectedUrl == '/creativeWorks'
    flash.message != null
  }

  void "Test the update action correctly persists"() {
    given:
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * save(_ as CreativeWork)
    }

    when:"The save action is executed with a valid instance"
    response.reset()
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'PUT'
    populateValidParams(params)
    def creativeWork = new CreativeWork(params)
    creativeWork.id = 1

    controller.update(creativeWork)

    then:"A redirect is issued to the show action"
    response.redirectedUrl == '/creativeWorks/1'
    controller.flash.message != null
  }

  void "Test the update action with an invalid instance"() {
    given:
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * save(_ as CreativeWork) >> { CreativeWork creativeWork ->
        throw new ValidationException("Invalid instance", creativeWork.errors)
      }
    }

    when:"The save action is executed with an invalid instance"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'PUT'
    controller.update(new CreativeWork())

    then:"The edit view is rendered again with the correct model"
    model.creativeWork != null
    view == 'edit'
  }

  void "Test the delete action with a null instance"() {
    when:"The delete action is called for a null instance"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'DELETE'
    controller.delete(null)

    then:"A 404 is returned"
    response.redirectedUrl == '/creativeWorks'
    flash.message != null
  }

  void "Test the delete action with an instance"() {
    given:
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * delete(2)
    }

    when:"The domain instance is passed to the delete action"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'DELETE'
    controller.delete(2)

    then:"The user is redirected to index"
    response.redirectedUrl == '/creativeWorks'
    flash.message != null
  }
  */
}






