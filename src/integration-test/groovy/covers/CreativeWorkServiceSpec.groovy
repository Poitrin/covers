package covers

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class CreativeWorkServiceSpec extends Specification {

  CreativeWorkService creativeWorkService
  SessionFactory sessionFactory

  static final MOCK_IP_ADDRESS_HASH = '-1234567';

  private Long setupData() {
    new CreativeWork(title: 'The Show Must Go On', artist: 'Queen', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    new CreativeWork(title: 'The Winner Takes It All', artist: 'ABBA', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    CreativeWork creativeWork = new CreativeWork(title: "It's My Life", artist: 'Bon Jovi', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    new CreativeWork(title: 'Can You Feel The Love Tonight', artist: 'Elton John', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    new CreativeWork(title: 'Imagine', artist: 'John Lennon', ipAddressHash: MOCK_IP_ADDRESS_HASH).save(flush: true, failOnError: true)
    
    creativeWork.id
  }

  void "test get"() {
    Long creativeWorkId = setupData()

    expect:
    creativeWorkService.get(creativeWorkId) != null
  }

  void "test list"() {
    setupData()

    when:
    List<CreativeWork> creativeWorkList = creativeWorkService.list(max: 2, offset: 2)

    then:
    creativeWorkList.size() == 2
    // TODO: Verify the correct instances are returned
  }

  void "test count"() {
    setupData()

    expect:
    creativeWorkService.count() == 5
  }

  void "test delete"() {
    Long creativeWorkId = setupData()

    expect:
    creativeWorkService.count() == 5

    when:
    creativeWorkService.delete(creativeWorkId)
    sessionFactory.currentSession.flush()

    then:
    creativeWorkService.count() == 4
  }

  void "test save"() {
    when:
    CreativeWork creativeWork = new CreativeWork(title: 'Mamma Mia', artist: 'ABBA', ipAddressHash: MOCK_IP_ADDRESS_HASH)
    creativeWorkService.save(creativeWork)

    then:
    creativeWork.id != null
  }
}