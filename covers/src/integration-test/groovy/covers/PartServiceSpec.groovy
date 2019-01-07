package covers

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class PartServiceSpec extends Specification {

  PartService partService
  SessionFactory sessionFactory

  static final MOCK_IP_ADDRESS_HASH = '-1234567'

  private Long setupData() {
    CreativeWork creativeWork = new CreativeWork(
      title: 'The Show Must Go On',
      artist: 'Queen',
      ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    new Part(creativeWork: creativeWork, name: 'Piano at the beginning', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    new Part(creativeWork: creativeWork, name: 'Synth guitar in the middle', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    Part part = new Part(creativeWork: creativeWork, name: 'Synth lead at the end', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    new Part(creativeWork: creativeWork, name: 'Vocal effect', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    new Part(creativeWork: creativeWork, name: 'Electric piano in the middle', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    
    part.id
  }

  void "test get"() {
    Long creativeWorkId = setupData()

    expect:
    partService.get(creativeWorkId) != null
  }

  void "test list"() {
    setupData()

    when:
    List<Part> partList = partService.list(max: 2, offset: 2)

    then:
    partList.size() == 2
    // TODO: Verify the correct instances are returned
  }

  void "test count"() {
    setupData()

    expect:
    partService.count() == 5
  }

  void "test delete"() {
    Long partId = setupData()

    expect:
    partService.count() == 5

    when:
    partService.delete(partId)
    sessionFactory.currentSession.flush()

    then:
    partService.count() == 4
  }

  void "test save"() {
    when:
    CreativeWork creativeWork = new CreativeWork(
      title: 'The Show Must Go On',
      artist: 'Queen',
      ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    Part part = new Part(
      creativeWork: creativeWork,
      name: 'Synth at the beginning',
      ipAddressHash: MOCK_IP_ADDRESS_HASH)
    partService.save(part)

    then:
    part.id != null
  }
}
