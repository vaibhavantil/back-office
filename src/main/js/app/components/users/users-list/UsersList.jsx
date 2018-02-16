import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { Label, List } from 'semantic-ui-react';
import { ItemContent } from 'components/shared';
import PaginatorList from 'components/shared/paginator-list/PaginatorList';
import * as sockets from 'sockets';

const ListItem = ({ item }) => (
    <Link to={`/members/${item.hid}`} replace>
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

ListItem.propTypes = {
    item: PropTypes.object.isRequired
};

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
            <React.Fragment>
                <PaginatorList
                    list={users}
                    itemContent={item => <ListItem item={item} />}
                    pageSize={10}
                />
            </React.Fragment>
        );
    }
}

UsersList.propTypes = {
    users: PropTypes.array.isRequired,
    newMessagesReceived: PropTypes.func.isRequired,
    client: PropTypes.object.isRequired
};
