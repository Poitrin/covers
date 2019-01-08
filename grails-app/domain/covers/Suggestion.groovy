package covers

class Suggestion {
  Part part
  String instrument
  Boolean approved = false

  // Timestamps
  Date dateCreated
  Date lastUpdated

  String ipAddressHash

  static constraints = {
    approved defaultValue: false
    instrument blank: false, minSize: 3
  }
}
