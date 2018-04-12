import moment from 'moment';

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

// TODO append field "newMessagesCounter" to each member
export const setNewMessagesCounter = (members /* counters */) => members;

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
    list.reduce((sum, payment) => sum + parseFloat(payment.amount), 0);

const setFieldsValues = (fields, data) =>
    fields.map(item => {
        const fieldInClaimData = data.find(data => data.name === item.name);
        if (fieldInClaimData) {
            return {
                ...item,
                value: fieldInClaimData.value,
                received: fieldInClaimData.received
            };
        } else {
            return item;
        }
    });

/**
 * Returns updated type object
 * @param {array} types
 * @param {object} claimData
 */
export const getActiveType = (types, claimData) => {
    const activeType = types.find(item => item.name === claimData.type);

    const requiredData = setFieldsValues(
        activeType.requiredData,
        claimData.data
    );
    const optionalData = setFieldsValues(
        activeType.optionalData,
        claimData.data
    );

    return {
        ...activeType,
        requiredData,
        optionalData
    };
};

/**
 * Hidding inactive members on first render && sort by signup date
 * @param {object} param0 -
 */
export const filterMembersList = ({ type, members }) =>
    type !== 'MEMBERS_REQUEST_SUCCESS'
        ? members
        : members
              .filter(item => item.status !== 'INACTIVATED')
              .map(item => ({
                  ...item,
                  signupDate: moment(
                      item.signupDate ||
                          // TODO remove mock
                          `03/${Math.floor(Math.random() * 15) + 1}/2018`
                  )
              }))
              .sort((a, b) => a.signupDate.diff(b.signupDate))
              .reverse();

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

/**
 * Sort array of questions by dates and answers
 * @param {object} questions arrays of answered/not answered questions
 */
export const sortQuestions = questions => ({
    answered: questions.answered.sort((a, b) =>
        moment(a.date).diff(moment(b.date))
    ),
    notAnswered: questions.notAnswered.sort((a, b) =>
        moment(a.date).diff(moment(b.date))
    )
});

/**
 * Replacing question from not answered to answered array
 * @param {object} questions arrays of answered/not answered questions
 * @param {object} data answered question
 */
export const replaceAnswer = (questions, data) => {
    let newAnswered;
    const notAnswered = questions.notAnswered.filter(item => {
        if (item.hid !== data.id) {
            return true;
        } else {
            newAnswered = { ...item, answer: data.msg };
            return false;
        }
    });
    return {
        notAnswered,
        answered: newAnswered
            ? [...questions.answered, newAnswered]
            : questions.answered
    };
};

/**
 * Returns string with member first+last name || member Id
 * @param {array} members
 * @param {string} id
 */
export const getMemberInfo = (members, id) => {
    const member = members.find(member => member.hid === id);
    return member
        ? `${member.firstName} ${member.lastName || ''}`
        : `id: ${id}`;
};

String.prototype.capitalize = function() {
    return typeof this === 'string'
        ? this.charAt(0).toUpperCase() + this.slice(1).toLowerCase()
        : this;
};

/**
 * Returns string splitted by words
 * @param {string} field
 * @example 'memberFirstName' -> 'Member first name'
 */
export const getFieldName = field =>
    field
        .match(/([A-Z]?[^A-Z]*)/g)
        .slice(0, -1)
        .join(' ')
        .capitalize();

export const getFieldValue = value => {
    if (!value) {
        return '';
    }
    if (Array.isArray(value)) {
        return value.join(', ');
    }
    if (value && typeof value === 'object' && value.constructor === Object) {
        return Object.keys(value).map(key => `${key}: ${value[key]}, `);
    }
    return value.toString();
};

export const sortAssetsList = assets =>
    assets
        .map(el => ({
            ...el,
            // TODO: remove data mock
            price: 1000,
            purchaseDate: `${Math.floor(Math.random() * 10 + 1)}/03/2020`
        }))
        .sort((a, b) => moment(a.registerDate).diff(moment(b.registerDate)));

/**
 * Sort members table (Members overview page)
 * @param {array} list members list
 * @param {string} fieldName clicked column name
 * @param {bool} isReverse 
 */
export const sortMembersList = (list, fieldName, isReverse) => {
    let sortedList = null;

    switch (fieldName) {
        case 'name':
            sortedList = list.sort(
                (a, b) =>
                    a.firstName + a.lastName > b.firstName + b.lastName ? 1 : -1
            );
            break;
        case 'birthday':
            sortedList = list.sort(
                (a, b) =>
                    a.birthDate && b.birthDate
                        ? moment(a.birthDate).diff(moment(b.birthDate))
                        : 0
            );
            break;
        default:
            sortedList = list;
    }
    return isReverse ? sortedList.reverse() : sortedList;
};

/**
 * Sort members table (MemberInsurance page)
 * @param {array} list membersList
 * @param {string} fieldName clicked column name
 * @param {bool} isReverse 
 */
export const sortMemberInsList = (list, fieldName, isReverse) => {
    let sortedList = null;

    switch (fieldName) {
        case 'name':
            sortedList = list.sort(
                (a, b) =>
                    `${a.memberFirstName}${a.memberLastName}` >
                    `${b.memberFirstName}${b.memberLastName}`
                        ? 1
                        : -1
            );
            break;
        case 'date':
            sortedList = list.sort(
                (a, b) =>
                    a.insuranceActiveFrom && b.insuranceActiveFrom
                        ? moment(a.insuranceActiveFrom).diff(
                              moment(b.insuranceActiveFrom)
                          )
                        : 0
            );
            break;

        case 'type':
            sortedList = list.sort(
                (a, b) => (a.insuranceType > b.insuranceType ? 1 : -1)
            );
            break;

        case 'status':
            sortedList = list.sort(
                (a, b) => (a.insuranceStatus > b.insuranceStatus ? 1 : -1)
            );
            break;

        default:
            sortedList = list;
    }
    return isReverse ? sortedList.reverse() : sortedList;
};
