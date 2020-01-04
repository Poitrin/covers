databaseChangeLog = {

    changeSet(author: "gradle (generated)", id: "1549806781244-1") {
        addColumn(tableName: "creative_work") {
            column(name: "youtube_video_id", type: "varchar(255)") {
                constraints(nullable: "true")
            }
        }
    }
}
