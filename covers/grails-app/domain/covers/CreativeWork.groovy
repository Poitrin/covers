package covers

class CreativeWork {
  String artist
  String title

  static constraints = {
    artist blank: false
    title blank: false
  }

  static hasMany = [parts: Part]

  // Sometimes sorting only works after restarting (and recompiling) the app
  static mapping = {
    parts sort: 'id', order: 'asc'
  }
}
