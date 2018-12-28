package covers

class Suggestion {
  Part part
  String instrument

  static constraints = {
    instrument blank: false, minSize: 3
  }
}
