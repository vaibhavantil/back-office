package com.hedvig.backoffice.services.updates;

public interface UpdatesService {

    void append(int count, UpdateType type);
    void subscribe(String email);
    void unsubscribe(String email);
    void send();

}
