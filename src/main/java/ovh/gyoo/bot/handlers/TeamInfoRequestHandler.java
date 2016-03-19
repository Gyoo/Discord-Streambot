package ovh.gyoo.bot.handlers;

import com.mb3364.twitch.api.handlers.BaseFailureHandler;
import com.mb3364.twitch.api.models.Channel;
import com.mb3364.twitch.api.models.Stream;

import java.util.List;

public interface TeamInfoRequestHandler extends BaseFailureHandler{
    void onSuccess(List<Stream> streams);
}
