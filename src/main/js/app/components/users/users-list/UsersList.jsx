import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import { Table } from 'semantic-ui-react';
import PaginatorList from 'components/shared/paginator-list/PaginatorList';
import * as sockets from 'sockets';
import { history } from 'app/store';
import {LinkRow} from 'components/shared'

export default class UsersList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            socket: null,
            subscription: null
        };
    }

    getUserName = user =>
        user.firstName
            ? `${user.firstName} ${user.lastName || ''}`
            : `User-${user.hid}`;

    linkClickHandler = id => history.push(`/members/${id}`);

    getTableRow = item => {
        const date = moment(item.birthDate);
        const formattedDate = date.isValid()
            ? date.format('DD MMMM YYYY')
            : '-';
        return (
            <LinkRow onClick={this.linkClickHandler.bind(this, item.hid)}>
                <Table.Cell>{this.getUserName(item)}</Table.Cell>
                <Table.Cell>{formattedDate}</Table.Cell>
            </LinkRow>
        );
    };

    getTableHeader = () => (
        <Table.Header>
            <Table.Row>
                <Table.HeaderCell>Name</Table.HeaderCell>
                <Table.HeaderCell>Birthday</Table.HeaderCell>
            </Table.Row>
        </Table.Header>
    );

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
                itemContent={item => this.getTableRow(item)}
                tableHeader={this.getTableHeader()}
                pageSize={10}
            />
        );
    }
}

UsersList.propTypes = {
    users: PropTypes.array.isRequired,
    newMessagesReceived: PropTypes.func.isRequired,
    client: PropTypes.object.isRequired
};
