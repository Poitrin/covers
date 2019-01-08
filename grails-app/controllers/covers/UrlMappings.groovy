package covers

class UrlMappings {

  static mappings = {
    "/creativeWorks" (resources: 'creativeWork') {
      "/parts" (resources: "part") {
        "/suggestions" (resources: "suggestion")
      }
    }
    "/creativeWorks/$id/parts" (resources: 'part', includes: ['save', 'update'])

    /*
    "/$controller/$action?/$id?(.$format)?"{
        constraints {
            // apply constraints here
        }
    }
    */

    "/"(redirect:"/creativeWorks")
    "500"(view:'/error')
    "404"(view:'/notFound')
  }
}
