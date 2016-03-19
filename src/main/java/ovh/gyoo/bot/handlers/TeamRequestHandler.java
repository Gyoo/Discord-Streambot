package ovh.gyoo.bot.handlers;

import com.mb3364.twitch.api.handlers.BaseFailureHandler;
import com.mb3364.twitch.api.models.Team;


public interface TeamRequestHandler extends BaseFailureHandler{
    void onSuccess(Team team);
}
