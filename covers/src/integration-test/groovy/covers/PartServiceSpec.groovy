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

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Part(...).save(flush: true, failOnError: true)
        //new Part(...).save(flush: true, failOnError: true)
        //Part part = new Part(...).save(flush: true, failOnError: true)
        //new Part(...).save(flush: true, failOnError: true)
        //new Part(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //part.id
    }

    void "test get"() {
        setupData()

        expect:
        partService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Part> partList = partService.list(max: 2, offset: 2)

        then:
        partList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
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
        assert false, "TODO: Provide a valid instance to save"
        Part part = new Part()
        partService.save(part)

        then:
        part.id != null
    }
}
