import React from 'react';
import PropTypes from 'prop-types';
import { Tab } from 'semantic-ui-react';
import styled from 'styled-components';
import ChatTab from './ChatTab';
import DetailsTab from './DetailsTab';
import ClaimsTab from './ClaimsTab';

const TabContainer = styled(Tab.Pane)`
    &&& {
        min-width: 700px;
    }
`;

const Details = ({ messages: { user } }) => (
    <TabContainer>
        <DetailsTab user={user} />
    </TabContainer>
);

Details.propTypes = {
    messages: PropTypes.object.isRequired
};

/* eslint-disable react/display-name */
export const memberPagePanes = (
    props,
    { addMessageHandler, claimsByUser },
    socket
) => [
    { menuItem: 'Details', render: () => <Details {...props} /> },
    {
        menuItem: 'Chat',
        render: () => (
            <TabContainer>
                <ChatTab
                    {...props}
                    addMessage={addMessageHandler}
                    socket={socket}
                />
            </TabContainer>
        )
    },
    {
        menuItem: 'Claims',
        render: () => (
            <TabContainer>
                <ClaimsTab {...props} claimsByUser={claimsByUser} />
            </TabContainer>
        )
    }
];
