package covers

import grails.gorm.services.Service

@Service(CreativeWork)
interface CreativeWorkService {

    CreativeWork get(Serializable id)

    List<CreativeWork> list(Map args)

    Long count()

    void delete(Serializable id)

    CreativeWork save(CreativeWork creativeWork)

}