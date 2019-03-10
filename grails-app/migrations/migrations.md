# Typical workflow of database migration

## Update .groovy models
Don't forget to add a default value and declare the new column as nullable, if necessary.
```groovy
class MyClass {
  MyType myField = null

  static constraints = {
    myField nullable: true
  }
}
```

## Write new DB structure to file
```
grails dbm-gorm-diff <yyyymmdd>.groovy --add
```
This can take >= 1 minute.

Verify that the new file contains the necessary changes, e.g. that nullable columns contain the necessary constraint: `constraints(nullable: "true")`.

## Execute DDL statements (= update DB tables)
```
For development:
grails dbm-update

For other environments:
grails (test/production) dbm-update
```
