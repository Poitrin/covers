databaseChangeLog = {

    changeSet(author: "gradle (generated)", id: "1551010687629-1") {
        addColumn(tableName: "part") {
            column(name: "timing", type: "int4") {
                constraints(nullable: "true")
            }
        }
    }
}
