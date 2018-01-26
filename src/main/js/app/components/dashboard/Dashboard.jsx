import React from 'react';
import styled from 'styled-components';
import { List, Label } from 'semantic-ui-react';
import { history } from 'app/app';
import * as sockets from 'app/lib/sockets';

const pages = [
    {
        text: 'Assets',
        route: '/assets'
    },
    {
        text: 'Users overview',
        route: '/users'
    },
    {
        text: 'Questions',
        route: ''
    },
    {
        text: 'Claims',
        route: '/claims'
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
    width: 300px;
`;

const LinkName = styled.span`
    font-family: Lato, 'Helvetica Neue', Arial, Helvetica, sans-serif;
    font-weight: 700;
    color: rgba(0, 0, 0, 0.87);
`;

const ItemContent = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
`;

export default class Dashboard extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const {
            setActiveConnection,
            messages,
            dashboardUpdated,
            errorReceived,
            match: { params }
        } = this.props;

        if (!messages.activeConnection) {
            sockets.connect().then(stompClient => {
                setActiveConnection(stompClient);
                sockets.dashboardSubscribe(
                    { dashboardUpdated, errorReceived },
                    params.id,
                    stompClient
                );
            });
        } else {
            sockets.dashboardSubscribe(
                { dashboardUpdated, errorReceived },
                params.id,
                messages.activeConnection
            );
        }
    }

    render() {
        const { unsetClient } = this.props;
        return (
            <DashboardContainer>
                <ListContainer>
                    <List
                        animated
                        size="massive"
                        verticalAlign="middle"
                        selection
                    >
                        {pages.map((item, id) => (
                            <List.Item
                                key={id}
                                onClick={redirect.bind(this, item.route)}
                            >
                                <ItemContent>
                                    <LinkName>{item.text}</LinkName>
                                    <Label color="blue" horizontal circular>
                                        {Math.floor(Math.random() * 10)}
                                    </Label>
                                </ItemContent>
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
    }
}
