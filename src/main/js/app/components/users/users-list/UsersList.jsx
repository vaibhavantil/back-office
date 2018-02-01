import React from 'react';
import { Link } from 'react-router-dom';
import { Label, List } from 'semantic-ui-react';
import { ItemContent } from '../../dashboard/Dashboard';
import PaginatorList from 'components/shared/paginator-list/PaginatorList';
import * as sockets from 'sockets';

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
            socket: null,
            subscription: null
        };
    }

    subscribeSocket = connection => {
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
    };

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
        return (
            <PaginatorList
                list={users}
                itemContent={item => <ListItem item={item} />}
            />
        );
    }
}
