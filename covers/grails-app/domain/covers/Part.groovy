package covers

class Part {
  CreativeWork creativeWork
  String name

  static constraints = {
    name blank: false, minSize: 3
  }

  static hasMany = [suggestions: Suggestion]

  static mapping = {
    suggestions sort: 'id', order: 'desc'
  }
}
