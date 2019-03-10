databaseChangeLog = {

    changeSet(author: "gradle (generated)", id: "1547982677780-1") {
        createSequence(sequenceName: "hibernate_sequence")
    }

    changeSet(author: "gradle (generated)", id: "1547982677780-2") {
        createTable(tableName: "creative_work") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "creative_workPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "title", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(defaultValueBoolean: "false", name: "approved", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "ip_address_hash", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "artist", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "gradle (generated)", id: "1547982677780-3") {
        createTable(tableName: "part") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "partPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "creative_work_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "approved", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "ip_address_hash", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "gradle (generated)", id: "1547982677780-4") {
        createTable(tableName: "suggestion") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "suggestionPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "part_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "instrument", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "approved", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "ip_address_hash", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "gradle (generated)", id: "1547982677780-5") {
        addForeignKeyConstraint(baseColumnNames: "creative_work_id", baseTableName: "part", constraintName: "FK7i3hnvra3742g3f096ynvodmj", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "creative_work")
    }

    changeSet(author: "gradle (generated)", id: "1547982677780-6") {
        addForeignKeyConstraint(baseColumnNames: "part_id", baseTableName: "suggestion", constraintName: "FKs70t6cn3h1mj4aqru2wx5t8xb", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "part")
    }
    include file: '20190210.groovy'
    include file: '20190224.groovy'
}
