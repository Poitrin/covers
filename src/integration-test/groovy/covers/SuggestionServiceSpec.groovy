package covers

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory
import org.apache.commons.lang.RandomStringUtils

@Integration
@Rollback
class SuggestionServiceSpec extends Specification {

  SuggestionService suggestionService
  UtilityService utilityService
  SessionFactory sessionFactory

  static final MOCK_IP_ADDRESS_HASH = '-1234567'

  private Long setupData() {
    CreativeWork creativeWork = new CreativeWork(
      title: 'The Show Must Go On',
      artist: 'Queen',
      youtubeVideoId: RandomStringUtils.randomAlphanumeric(11),
      ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    new Part(creativeWork: creativeWork, name: 'Synth guitar in the middle', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    Part part = new Part(creativeWork: creativeWork, name: 'Synth lead at the end', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)

    new Suggestion(part: part, instrument: 'Virus TI, Preset 12345', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    new Suggestion(part: part, instrument: 'Phoenix 4000, Preset 4.05', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    Suggestion suggestion = new Suggestion(part: part, instrument: 'Moog Synth, Preset XYZ', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    new Suggestion(part: part, instrument: 'Nord Electro, Preset 3.14', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    new Suggestion(part: part, instrument: 'Apple Logic Pro X, Ambient Electro', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)

    suggestion.id
  }

  void "test get"() {
    Long suggestionId = setupData()

    expect:
    suggestionService.get(suggestionId) != null
  }

  void "test list"() {
    setupData()

    when:
    List<Suggestion> suggestionList = suggestionService.list(max: 2, offset: 2)

    then:
    suggestionList.size() == 2
    // TODO: Verify the correct instances are returned
  }

  void "test count"() {
    setupData()

    expect:
    suggestionService.count() == 5
  }

  void "test countByDateCreatedGreaterThan"() {
    setupData()

    expect:
    suggestionService.countByDateCreatedGreaterThan(utilityService.LAST_MIDNIGHT) == 5
  }

  void "test countByIpAddressHashAndDateCreatedGreaterThan"() {
    setupData()

    expect:
    suggestionService.countByIpAddressHashAndDateCreatedGreaterThan(MOCK_IP_ADDRESS_HASH, utilityService.LAST_MIDNIGHT) == 5
    suggestionService.countByIpAddressHashAndDateCreatedGreaterThan('-5275184', utilityService.LAST_MIDNIGHT) == 0
  }

  void "test delete"() {
    Long suggestionId = setupData()

    expect:
    suggestionService.count() == 5

    when:
    suggestionService.delete(suggestionId)
    sessionFactory.currentSession.flush()

    then:
    suggestionService.count() == 4
  }

  void "test save"() {
    when:
    CreativeWork creativeWork = new CreativeWork(
      title: 'The Show Must Go On',
      artist: 'Queen',
      youtubeVideoId: RandomStringUtils.randomAlphanumeric(11),
      ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    Part part = new Part(
      creativeWork: creativeWork,
      name: 'Synth at the beginning',
      ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    Suggestion suggestion = new Suggestion(
      part: part,
      instrument: 'Nord Electro, Preset 4',
      ipAddressHash: MOCK_IP_ADDRESS_HASH)

    suggestionService.save(suggestion)

    then:
    suggestion.id != null
  }
}
