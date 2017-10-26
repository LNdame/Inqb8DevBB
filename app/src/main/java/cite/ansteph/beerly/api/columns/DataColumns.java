package cite.ansteph.beerly.api.columns;

import android.provider.BaseColumns;

/**
 * Created by loicstephan on 2017/08/28.
 */

public interface DataColumns extends BaseColumns {
    /** UUID of data. */
    String ID = "uuid";

    /** Sync columns. */
    String USER = "user";
    String DELETED = "deleted";
    String UPDATED = "updated";
}
