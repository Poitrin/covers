databaseChangeLog = {

    changeSet(author: "gradle (generated)", id: "1549806781244-1") {
        addColumn(tableName: "creative_work") {
            column(name: "youtube_video_id", type: "varchar(255)") {
                constraints(nullable: "true")
            }
        }
    }

    changeSet(author: "gradle (generated)", id: "1549806781244-2") {
        addDefaultValue(columnDataType: "bigint", columnName: "id", defaultValueComputed: "nextval('creative_work_id_seq'::regclass)", tableName: "creative_work")
    }

    changeSet(author: "gradle (generated)", id: "1549806781244-3") {
        addDefaultValue(columnDataType: "bigint", columnName: "id", defaultValueComputed: "nextval('part_id_seq'::regclass)", tableName: "part")
    }

    changeSet(author: "gradle (generated)", id: "1549806781244-4") {
        addDefaultValue(columnDataType: "bigint", columnName: "id", defaultValueComputed: "nextval('suggestion_id_seq'::regclass)", tableName: "suggestion")
    }
}
