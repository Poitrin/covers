package covers

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*
import org.apache.commons.lang.RandomStringUtils

class SuggestionControllerSpec extends Specification implements ControllerUnitTest<SuggestionController>, DomainUnitTest<Suggestion> {
  static final MOCK_PART_NAME = 'Piano at the beginning of the song'
  static final MOCK_IP_ADDRESS_HASH = '-1234567'

  // TODO: this has been copied from PartControllerSpec
  CreativeWork createCreativeWork() {
    CreativeWork creativeWork = new CreativeWork(
      title: 'Lady Madonna',
      artist: 'The Beatles',
      youtubeVideoId: RandomStringUtils.randomAlphanumeric(11),
      ipAddressHash: MOCK_IP_ADDRESS_HASH)
    assert creativeWork.validate() == true
    
    creativeWork.save()
  }

  Part createPart(CreativeWork creativeWork) {
    Part part = new Part(
      creativeWork: creativeWork,
      name: MOCK_PART_NAME,
      ipAddressHash: MOCK_IP_ADDRESS_HASH)
    assert part.validate() == true
    
    part.save()
  }

  void "Test the index action returns the correct model"() {
    when:"The index action is executed"
    controller.index()

    then:"An error is thrown because the method does not exist"
    thrown MissingMethodException
  }

  void "Test the create action returns the correct model"() {
    when:"The create action is executed"
    controller.create()

    then:"An error is thrown because the method does not exist"
    thrown MissingMethodException
  }

  // TODO: If suggestion is read from command object ...
  /*
  void "Test the save action with a null instance"() {
    when:"Save is called for a domain instance that doesn't exist"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'POST'
    controller.save()

    then:"A 404 error is returned"
    response.redirectedUrl == '/suggestion/index'
    flash.message != null
  }
  */

  void "Test the save action correctly persists"() {
    given:
    CreativeWork creativeWork = createCreativeWork()
    Part part = createPart(creativeWork)
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * get(_) >> creativeWork
    }
    controller.partService = Mock(PartService) {
      1 * get(_) >> part
      1 * save(_)
    }
    controller.suggestionService = Mock(SuggestionService) {
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

    params.creativeWorkId = creativeWork.id
    params.partId = part.id
    params.instrument = 'Nord Stage 3 - Grand Piano XL' // Suggestion

    controller.save()

    then:"A redirect is issued to the show action"
    response.redirectedUrl == ('/creativeWorks/' + creativeWork.id)
    controller.flash.message != null
  }

  void "Test the save action with an invalid instance"() {
    given:
    CreativeWork creativeWork = createCreativeWork()
    Part part = createPart(creativeWork)
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * get(_) >> creativeWork
    }
    controller.partService = Mock(PartService) {
      1 * get(_) >> part
    }
    controller.suggestionService = Mock(SuggestionService) {
      1 * countByDateCreatedGreaterThan(_) >> 0
      1 * countByIpAddressHashAndDateCreatedGreaterThan(_, _) >> 0
    }
    controller.utilityService = Mock(UtilityService) {
      1 * getCurrentUserIpAddressHash(_) >> MOCK_IP_ADDRESS_HASH
    }

    when:"The save action is executed with an invalid instance"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'POST'

    params.creativeWorkId = creativeWork.id
    params.partId = part.id
    // We do not set params.instrument.
    
    controller.save()

    then:"The creativeWork/show view is rendered again with the correct model"
    model.creativeWork != null
    model.part != null
    model.suggestion != null
    view == '/creativeWork/show'
  }

  void "Test the save action does not allow too many inserts per day"() {
    given:
    CreativeWork creativeWork = createCreativeWork()
    Part part = createPart(creativeWork)
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * get(_) >> creativeWork
    }
    controller.partService = Mock(PartService) {
      1 * get(_) >> part
      0 * save(_)
    }
    controller.suggestionService = Mock(SuggestionService) {
      1 * countByDateCreatedGreaterThan(_) >> SuggestionController.MAX_ITEMS_ALLOWED_TODAY
      0 * countByIpAddressHashAndDateCreatedGreaterThan(_, _) >> 0
      0 * save(_ as Suggestion)
    }
    controller.utilityService = Mock(UtilityService) {
      1 * getCurrentUserIpAddressHash(_) >> "-12345"
    }

    when:"The save action is executed with a valid instance"
    response.reset()
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'POST'

    params.creativeWorkId = creativeWork.id
    params.partId = part.id
    params.instrument = 'Nord Stage 3 - Grand Piano XL' // Suggestion

    controller.save()

    then:"A redirect is issued to the show action"
    response.redirectedUrl == ('/creativeWorks/' + creativeWork.id)
    controller.flash.message == 'default.too.many.requests.message'
  }

  void "Test the save action does not allow too many inserts by the same user per day"() {
    given:
    CreativeWork creativeWork = createCreativeWork()
    Part part = createPart(creativeWork)
    controller.creativeWorkService = Mock(CreativeWorkService) {
      1 * get(_) >> creativeWork
    }
    controller.partService = Mock(PartService) {
      1 * get(_) >> part
      0 * save(_)
    }
    controller.suggestionService = Mock(SuggestionService) {
      1 * countByDateCreatedGreaterThan(_) >> SuggestionController.MAX_ITEMS_ALLOWED_TODAY
      0 * countByIpAddressHashAndDateCreatedGreaterThan(_, _) >> 0
      0 * save(_ as Suggestion)
    }
    controller.utilityService = Mock(UtilityService) {
      1 * getCurrentUserIpAddressHash(_) >> "-12345"
    }

    when:"The save action is executed with a valid instance"
    response.reset()
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'POST'

    params.creativeWorkId = creativeWork.id
    params.partId = part.id
    params.instrument = 'Nord Stage 3 - Grand Piano XL' // Suggestion

    controller.save()

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
    response.redirectedUrl == '/suggestion/index'
    flash.message != null
  }

  void "Test the update action correctly persists"() {
    given:
    controller.suggestionService = Mock(SuggestionService) {
      1 * save(_ as Suggestion)
    }

    when:"The save action is executed with a valid instance"
    response.reset()
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'PUT'
    populateValidParams(params)
    def suggestion = new Suggestion(params)
    suggestion.id = 1

    controller.update(suggestion)

    then:"A redirect is issued to the show action"
    response.redirectedUrl == '/suggestion/show/1'
    controller.flash.message != null
  }

  void "Test the update action with an invalid instance"() {
    given:
    controller.suggestionService = Mock(SuggestionService) {
      1 * save(_ as Suggestion) >> { Suggestion suggestion ->
        throw new ValidationException("Invalid instance", suggestion.errors)
      }
    }

    when:"The save action is executed with an invalid instance"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'PUT'
    controller.update(new Suggestion())

    then:"The edit view is rendered again with the correct model"
    model.suggestion != null
    view == 'edit'
  }

  void "Test the delete action with a null instance"() {
    when:"The delete action is called for a null instance"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'DELETE'
    controller.delete(null)

    then:"A 404 is returned"
    response.redirectedUrl == '/suggestion/index'
    flash.message != null
  }

  void "Test the delete action with an instance"() {
    given:
    controller.suggestionService = Mock(SuggestionService) {
      1 * delete(2)
    }

    when:"The domain instance is passed to the delete action"
    request.contentType = FORM_CONTENT_TYPE
    request.method = 'DELETE'
    controller.delete(2)

    then:"The user is redirected to index"
    response.redirectedUrl == '/suggestion/index'
    flash.message != null
  }
  */
}
