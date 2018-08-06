package com.hedvig.backoffice.services.updates;

import com.hedvig.backoffice.security.AuthorizationException;

public interface UpdatesService {

  void changeOn(long count, UpdateType type);

  void set(long count, UpdateType type);

  void updates(String personnelId);

  void subscribe(String personnelId, String sessionId) throws AuthorizationException;

  void unsubscribe(String personnelId, String sessionId);
}
