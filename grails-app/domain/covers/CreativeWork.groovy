package covers

class CreativeWork {
  String artist
  String title
  Boolean approved = false

  // Timestamps
  // From the doc: By default when you have properties called dateCreated and/or lastUpdated in a domain class,
  // Grails automatically maintains their state in the database
  Date dateCreated
  Date lastUpdated

  String ipAddressHash

  static constraints = {
    artist blank: false
    title blank: false
  }

  static hasMany = [parts: Part]

  /**
   * Returns all parts that the current user is allowed to see.
   * These are the parts that have been approved, or that have been created by the current user (same IP address, hashed)
   */
  Part[] approvedParts(String ipAddressHash) {
    if (ipAddressHash == null) {
      throw new RuntimeException("ipAddressHash must not be null")
    }
    // TODO: Use Grails criteria oder something else to filter via SQL instead of Groovy
    return this.parts.findAll { it.approved || (it.ipAddressHash == ipAddressHash) }
  }

  static mapping = {
    approved defaultValue: "false"
    // Sometimes sorting only works after restarting (and recompiling) the app
    parts sort: 'id', order: 'asc'
  }
}
