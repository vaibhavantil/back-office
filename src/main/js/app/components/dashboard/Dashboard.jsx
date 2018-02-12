import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { List, Label } from 'semantic-ui-react';
import { history } from 'app/store';
import * as sockets from 'sockets';
import { routesList } from 'app/lib/selectOptions';

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

export const ItemContent = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
`;

export default class Dashboard extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            socket: null,
            subscription: null
        };
    }

    subscribeSocket = connection => {
        const {
            dashboardUpdated,
            dashboardErrorReceived,
            updatesRequestSuccess,
            client: { user }
        } = this.props;
        const { stompClient, subscription } = sockets.dashboardSubscribe(
            { dashboardUpdated, dashboardErrorReceived, updatesRequestSuccess },
            user,
            connection
        );
        this.setState({
            socket: stompClient,
            subscription
        });
    };

    redirect = (route, type) => {
        this.props.cleanupDashboardItem(type, this.state.socket);
        history.push(route);
    };

    getItemContent = item => {
        const { dashboard: { data } } = this.props;

        return (
            <ItemContent>
                <LinkName>{item.text}</LinkName>
                {data && data[item.type] ? (
                    <Label color="blue" horizontal circular>
                        {data[item.type]}
                    </Label>
                ) : null}
            </ItemContent>
        );
    };

    componentDidMount() {
        const { setActiveConnection, messages } = this.props;

        if (!messages.activeConnection) {
            sockets.connect().then(stompClient => {
                setActiveConnection(stompClient);
                this.subscribeSocket(stompClient);
            });
        } else {
            this.subscribeSocket(messages.activeConnection);
        }
    }

    componentWillUnmount() {
        sockets.disconnect(null, this.state.subscription);
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
                        {routesList.map((item, id) => (
                            <List.Item
                                key={id}
                                onClick={this.redirect.bind(
                                    this,
                                    item.route,
                                    item.type
                                )}
                            >
                                {this.getItemContent(item)}
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

Dashboard.propTypes = {
    unsetClient: PropTypes.func.isRequired,
    setActiveConnection: PropTypes.func.isRequired,
    messages: PropTypes.object.isRequired,
    dashboard: PropTypes.object.isRequired,
    cleanupDashboardItem: PropTypes.func.isRequired,
    dashboardUpdated: PropTypes.func.isRequired,
    dashboardErrorReceived: PropTypes.func.isRequired,
    updatesRequestSuccess: PropTypes.func.isRequired,
    client: PropTypes.object.isRequired
};
