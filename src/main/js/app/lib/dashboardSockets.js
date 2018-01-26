import config from 'app/api/config';

const connectError = { stompClient: null, subscription: null };

const responseHandler = (actions, response) => {
    const parsedRes = JSON.parse(response.body);
    const data = parsedRes.payload;

    if (parsedRes.type === 'ERROR') {
        actions.errorReceived(data);
        return;
    }
    actions.dashboardUpdated(data);
};

export const subscribe = (actions, userId, stompClient) => {
    if (stompClient) {
        try {
            const subscription = stompClient.subscribe(
                config.ws.messages + userId,
                responseHandler.bind(this, actions)
            );
            stompClient.send(config.ws.history + userId);
            return { stompClient, subscription };
        } catch (error) {
            return connectError;
        }
    } else {
        return connectError;
    }
};
