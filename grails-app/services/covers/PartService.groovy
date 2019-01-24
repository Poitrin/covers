package covers

import grails.gorm.services.Service

@Service(Part)
interface PartService {

  Part get(Serializable id)

  List<Part> list(Map args)

  Long count()
  Long countByDateCreatedGreaterThan(Date dateCreated)
  Long countByIpAddressHashAndDateCreatedGreaterThan(String ipAddressHash, Date dateCreated)

  void delete(Serializable id)

  Part save(Part part)

}