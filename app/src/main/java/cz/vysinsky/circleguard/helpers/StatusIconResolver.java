package cz.vysinsky.circleguard.helpers;

import cz.vysinsky.circleguard.R;
import cz.vysinsky.circleguard.model.entities.Build;

public class StatusIconResolver {

    public static int resolve(int status) {
        switch (status) {
            case Build.STATUS_GREEN:
                return R.drawable.ic_status_green;
            case Build.STATUS_BLUE:
                return R.drawable.ic_status_blue;
            case Build.STATUS_ORANGE:
                return R.drawable.ic_status_orange;
            case Build.STATUS_RED:
                return R.drawable.ic_status_red;
            case Build.STATUS_GRAY:
            default:
                return R.drawable.ic_status_grey;
        }
    }

}
