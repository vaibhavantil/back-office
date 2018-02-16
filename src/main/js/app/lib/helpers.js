export const filterList = (filter, list, fieldName) =>
    list.filter(item => item[fieldName] === filter);

export const sortByKey = key => (a, b) => a[key] - b[key];

export const updateList = (list, msg) => {
    if (msg.length > 1) {
        return [...list, ...msg].sort(sortByKey('globalId'));
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

export const sliceList = (list, size = 100) =>
    list.length > size ? list.slice(-size) : list;

export const refreshMessagesList = (list, message, size) => {
    const slicedList = sliceList(list, size);
    const sorted = slicedList.sort(sortByKey('globalId'));
    return updateList(sorted, message);
};

// TODO append field "newMessagesCounter" to each user
export const setNewMessagesCounter = (users /* counters */) => users;

export const fieldsToArray = (fields, type) => {
    const result = {};
    Object.keys(type).forEach(fieldName => {
        result[fieldName] = Object.keys(fields[fieldName]).map(key => ({
            ...type[fieldName].find(el => el.name === key),
            name: key,
            value: fields[fieldName][key]
        }));

        if (result[fieldName].length !== type[fieldName].length) {
            result[fieldName] = [...type[fieldName], ...result[fieldName]];
        }
    });

    return result;
};

export const updateTypesList = (list, info) =>
    list.map(
        item =>
            item.name === info.type
                ? {
                      ...item,
                      ...fieldsToArray(info.details, {
                          additional: item.additional,
                          required: item.required
                      })
                  }
                : item
    );

export const getSum = list => {
    return list.reduce((sum, payment) => sum + payment.amount, 0);
};

export const getActiveType = (types, typeName) =>
    types.find(item => item.name === typeName);

export const updatePayments = (list, updated) =>
    list.map(
        item =>
            item.id === updated.id ? { ...item, amount: updated.amount } : item
    );

export const filterUsersList = ({ type, users }) => {
    if (type !== 'USERS_REQUEST_SUCCESS') return users;
    return users.filter(item => item.status !== 'INACTIVATED');
};
