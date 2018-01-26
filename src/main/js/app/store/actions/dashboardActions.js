import {
    CLEANUP_DASHDBOARD_ITEM,
    DASHBOARD_ERROR_RECEIVED,
    DASHBOARD_UADATED
} from 'constants';

export const cleanupDashboardItem = name => ({
    type: CLEANUP_DASHDBOARD_ITEM,
    name
});

export const dashboardErrorReceived = error => ({
    type: DASHBOARD_ERROR_RECEIVED,
    error
});

export const dashboardUpdated = status => ({
    type: DASHBOARD_UADATED,
    status
});
