export const filterList = (filter, list) =>
    list.filter(item => item.state === filter);

export const sortByKey = key => (a, b) => a[key] > b[key];

export const updateList = (list, msg) => {
    if (msg.length > 1) {
        return [...list, ...msg].sort(sortByKey('globalId'))
    } else {
        const result = list.slice();
        const updatedMessage = result.find((item, id) => {
            if (item.globalId === msg[0].globalId) {
                list[id] = { ...msg[0] };
            }
        });
        if (!updatedMessage) return [...result, ...msg];
        return [...result];
    }
};

export const refreshMessagesList = (list, message) => {
    const sorted = list.sort(sortByKey('globalId'));
    return updateList(sorted, message);
};
