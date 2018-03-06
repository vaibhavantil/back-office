package com.hedvig.backoffice.services.updates;

import com.hedvig.backoffice.security.AuthorizationException;

public interface UpdatesService {

    void changeOn(long count, UpdateType type);
    void set(long count, UpdateType type);
    void init(String email) throws AuthorizationException;
    void clear(String email, UpdateType type);
    void updates(String email);

}
