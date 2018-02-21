/**
 * Filter array of objects by obj field
 * @param {string} filter filter name
 * @param {array} list 
 * @param {string} fieldName 
 */
export const filterList = (filter, list, fieldName) =>
    list.filter(item => item[fieldName] === filter);

/**
 * Sort arary by field name
 * @param {string} key 
 */
export const sortByKey = key => (a, b) => a[key] - b[key];

/**
 * Updating message in array of messages by global id
 * @param {array} list 
 * @param {array} msg 
 */
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

/**
 * Slice array 
 * @param {array} list array 
 * @param {number} size slice size
 */
export const sliceList = (list, size = 100) =>
    list.length > size ? list.slice(-size) : list;

/**
 * Refreshing array of messages in chat
 * @param {array} list 
 * @param {object} message 
 * @param {number} size slice size
 */
export const refreshMessagesList = (list, message, size) => {
    const slicedList = sliceList(list, size);
    const sorted = slicedList.sort(sortByKey('globalId'));
    return updateList(sorted, message);
};

// TODO append field "newMessagesCounter" to each user
export const setNewMessagesCounter = (users /* counters */) => users;

/**
 * Updating array of claims types
 * @param {array} list 
 */
export const updateTypesList = list =>
    list.map(item => {
        const updated = { ...item };
        delete updated.requiredData;
        delete updated.optionalData;
        return updated;
    });

/**
 * Calc sum of claim payments
 * @param {array} list 
 */
export const getSum = list =>
    list.reduce((sum, payment) => sum + payment.amount, 0);

export const getActiveType = (types, typeName) =>
    types.find(item => item.name === typeName);

export const updatePayments = (list, updated) =>
    list.map(
        item =>
            item.id === updated.id ? { ...item, amount: updated.amount } : item
    );

/**
 * Hidding inactive users on first render 
 * @param {object} param0 -  
 */
export const filterUsersList = ({ type, users }) =>
    type !== 'USERS_REQUEST_SUCCESS'
        ? users
        : users.filter(item => item.status !== 'INACTIVATED');

/**
 * Updating state of claims details fields
 * @param {array} fieldsData current active fields
 * @param {array} currentType current active type
 * @param {string} fieldName field name to update
 * @param {string} value new value of field
 */
export const getClaimFieldsData = (
    fieldsData,
    currentType,
    fieldName,
    value
) => {
    const existFieldObj = fieldsData.find(item => item.name === fieldName);
    const fieldObj = currentType.find(item => item.name === fieldName);
    if (!fieldsData.length) {
        return [{ ...fieldObj, value }];
    }
    return existFieldObj
        ? fieldsData.map(
              item =>
                  item.name === fieldName ? { ...existFieldObj, value } : item
          )
        : [...fieldsData, { ...fieldObj, value }];
};
