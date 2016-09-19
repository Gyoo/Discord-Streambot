package common;

import com.ninja_squad.dbsetup.operation.Operation;

import static com.ninja_squad.dbsetup.Operations.*;

public class CommonOperations {

    public static final Operation TRUNCATE_ALL =
            sequenceOf(
                    sql("UNLOCK TABLES"),
                    deleteAllFrom("channel", "game", "manager", "notification", "permission", "queueitem", "stream", "tag", "team", "guild")
            );



    public static final Operation INSERT_COMMON_DATA =
            insertInto("guild")
                .row()
                    .column("ServerID", 131483070464393216L)
                    .column("ChannelID", 131483070464393216L)
                    .column("isCompact", 0)
                    .column("isActive", 1)
                    .column("Cleanup", 0)
                    .end()
                .build();

    public static final Operation INSERT_PERMISSION_ADD =
            insertInto("permission")
                .row()
                    .column("ServerID", 131483070464393216L)
                    .column("RoleID", 0)
                    .column("CommandID", 1L)
                    .column("Level", 0)
                    .end()
                .build();

    public static final Operation INSERT_MANAGER =
            insertInto("manager")
                .row()
                    .column("ServerID", 131483070464393216L)
                    .column("UserID", 63263941735755776L)
                    .end()
                .build();

}
