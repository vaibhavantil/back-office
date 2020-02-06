package com.hedvig.backoffice.services.updates;

import com.hedvig.backoffice.security.AuthorizationException;

public interface UpdatesService {

  void changeOn(long count, UpdateType type);

  void set(long count, UpdateType type);

  void updates(String personnelEmail);

  void subscribe(String personnelEmail, String sessionId) throws AuthorizationException;

  void unsubscribe(String personnelEmail, String sessionId);
}
