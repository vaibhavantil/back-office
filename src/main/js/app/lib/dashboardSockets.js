import config from 'app/api/config';

const connectError = { stompClient: null, subscription: null };

const responseHandler = (actions, response) => {
    const data = JSON.parse(response.body);

    if (data.type === 'ERROR') {
        actions.errorReceived(data);
        return;
    }
    const result = {};

    data.forEach(item => {
        result[item.type] = item.count;
    });
    actions.dashboardUpdated(result);
};

export const subscribe = (actions, user, stompClient) => {
    if (stompClient) {
        try {
            const subscription = stompClient.subscribe(
                `${config.ws.dashboardSub}${user}/updates`,
                responseHandler.bind(this, actions)
            );
            stompClient.send(config.ws.dashboardUpdates);
            return { stompClient, subscription };
        } catch (error) {
            return connectError;
        }
    } else {
        return connectError;
    }
};
