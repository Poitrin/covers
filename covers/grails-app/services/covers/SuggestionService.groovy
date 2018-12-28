package covers

import grails.gorm.services.Service

@Service(Suggestion)
interface SuggestionService {

    Suggestion get(Serializable id)

    List<Suggestion> list(Map args)

    Long count()

    void delete(Serializable id)

    Suggestion save(Suggestion suggestion)

}