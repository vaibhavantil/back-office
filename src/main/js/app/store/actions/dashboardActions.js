import {
    CLEANUP_DASHDBOARD_ITEM,
    DASHBOARD_ERROR_RECEIVED,
    DASHBOARD_UPDATED,
    UPDATES_REQUEST_SUCCESS
} from 'constants';

export const cleanupDashboardItem = (name, socket) => ({
    type: CLEANUP_DASHDBOARD_ITEM,
    name,
    socket
});

export const dashboardErrorReceived = error => ({
    type: DASHBOARD_ERROR_RECEIVED,
    error
});

export const updatesRequestSuccess = status => ({
    type: UPDATES_REQUEST_SUCCESS,
    status
});

export const dashboardUpdated = status => ({
    type: DASHBOARD_UPDATED,
    status
});
