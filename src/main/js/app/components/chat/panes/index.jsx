import React from 'react';
import PropTypes from 'prop-types';
import { Tab } from 'semantic-ui-react';
import styled from 'styled-components';
import ChatTab from './ChatTab';
import DetailsTab from './DetailsTab';
import ClaimsTab from './ClaimsTab';
import InsuranceTab from './InsuranceTab';

const TabContainer = styled(Tab.Pane)`
    &&& {
        min-width: 700px;
    }
`;

const TabItem = ({ props, TabContent }) => {
    return (
        <TabContainer>
            <TabContent {...props} />
        </TabContainer>
    );
};
TabItem.propTypes = {
    props: PropTypes.object.isRequired,
    TabContent: PropTypes.func.isRequired
};

/* eslint-disable react/display-name */
export const memberPagePanes = (props, addMessage, socket) => [
    {
        menuItem: 'Details',
        render: () => <TabItem props={props} TabContent={DetailsTab} />
    },
    {
        menuItem: 'Chat',
        render: () => (
            <TabItem
                TabContent={ChatTab}
                props={{ ...props, addMessage, socket }}
            />
        )
    },
    {
        menuItem: 'Claims',
        render: () => <TabItem props={props} TabContent={ClaimsTab} />
    },
    {
        menuItem: 'Insurance',
        render: () => <TabItem props={props} TabContent={InsuranceTab} />
    }
];
