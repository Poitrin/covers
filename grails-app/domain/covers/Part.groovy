package covers

class Part {
  CreativeWork creativeWork
  String name
  Boolean approved = false

  // Timestamps
  Date dateCreated
  Date lastUpdated

  String ipAddressHash

  static constraints = {
    name blank: false, minSize: 3
  }

  static hasMany = [suggestions: Suggestion]

  Suggestion[] approvedSuggestions(String ipAddressHash) {
    // TODO: Use Grails criteria oder something else to filter via SQL instead of Groovy
    return this.suggestions.findAll { it.approved || (it.ipAddressHash == ipAddressHash) }
  }

  static mapping = {
    approved defaultValue: false
    suggestions sort: 'id', order: 'desc'
  }
}
