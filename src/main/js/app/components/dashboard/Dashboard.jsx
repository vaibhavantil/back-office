import React from 'react';
import styled from 'styled-components';
import { List } from 'semantic-ui-react';
import { history } from 'app/app';

const pages = [
    {
        text: 'Assets',
        route: '/assets'
    },
    {
        text: 'Users overview',
        route: '/messages'
    },
    {
        text: 'Questions',
        route: ''
    },
    {
        text: 'Claims',
        route: ''
    }
];

const redirect = route => {
    history.push(route);
};

const DashboardContainer = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 70%;

`;

const ListContainer = styled.div`
    width: 600px;
`;

const Dashboard = ({ unsetClient }) => (
    <DashboardContainer>
        <ListContainer>
            <List animated verticalAlign="middle" size="massive" selection>
                {pages.map((item, id) => (
                    <List.Item
                        key={id}
                        onClick={redirect.bind(this, item.route)}
                    >
                        <List.Content>
                            <List.Header>{item.text}</List.Header>
                        </List.Content>
                    </List.Item>
                ))}
                <List.Item onClick={unsetClient}>
                    <List.Content>
                        <List.Header>Logout</List.Header>
                    </List.Content>
                </List.Item>
            </List>
        </ListContainer>
    </DashboardContainer>
);

export default Dashboard;
