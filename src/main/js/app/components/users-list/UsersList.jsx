import React from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import { Label, List } from 'semantic-ui-react';
import Pagination from '../pagination/Pagination';
import { ItemContent } from '../dashboard/Dashboard';
import * as sockets from 'socketsLib';

const ListContainer = styled.div`
    width: 500px;
    border: solid 1px #dfe0e0;
    border-radius: 5px;
    padding: 20px 10px;
    margin-top: 30px;
`;

const ListItem = ({ item }) => (
    <Link to={`/users/${item.hid}`} replace>
        <ItemContent>
            {item.firstName ? (
                <List.Header>
                    {`${item.firstName} ${item.lastName || ''}`}
                </List.Header>
            ) : (
                <List.Header>User-{item.hid}</List.Header>
            )}
            {item.newMessages && (
                <Label color="blue" horizontal circular>
                    {item.newMessages}
                </Label>
            )}
        </ItemContent>
    </Link>
);

export default class UsersList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeList: [],
            socket: null,
            subscription: null
        };
        this.onChangePage = this.onChangePage.bind(this);
        this.subscribeSocket = this.subscribeSocket.bind(this);
    }

    onChangePage(activeList) {
        this.setState({ activeList });
    }

    subscribeSocket(connection) {
        const { newMessagesReceived, client: { name } } = this.props;
        const { stompClient, subscription } = sockets.usersListSubscribe(
            { newMessagesReceived },
            name,
            connection
        );
        this.setState({
            socket: stompClient,
            subscription
        });
    }

    componentDidMount() {
        // TODO uncomment when ready method to count the number of unread messages

        /* const { setActiveConnection, messages } = this.props;
        if (!messages.activeConnection) {
            sockets.connect().then(stompClient => {
                setActiveConnection(stompClient);
                this.subscribeSocket(stompClient);
            });
        } else {
            this.subscribeSocket(messages.activeConnection);
        } */
    }

    render() {
        const { users } = this.props;
        const { activeList } = this.state;
        return (
            <ListContainer>
                <List selection size="big">
                    {activeList.length ? (
                        activeList.map(item => (
                            <List.Item key={item.hid}>
                                <ListItem item={item} />
                            </List.Item>
                        ))
                    ) : (
                        <h2>Not found</h2>
                    )}
                </List>
                <Pagination
                    items={users}
                    onChangePage={this.onChangePage}
                    pageSize={6}
                />
            </ListContainer>
        );
    }
}
