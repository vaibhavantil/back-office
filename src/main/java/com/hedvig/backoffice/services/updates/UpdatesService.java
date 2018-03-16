package com.hedvig.backoffice.services.updates;

import com.hedvig.backoffice.security.AuthorizationException;

public interface UpdatesService {

    void changeOn(long count, UpdateType type);
    void set(long count, UpdateType type);
    void init(String id) throws AuthorizationException;
    void updates(String id);

}
